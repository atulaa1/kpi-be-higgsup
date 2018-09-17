package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.LateTimeCheckDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiLateTimeCheck;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.entity.KpiYearMonth;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiLateTimeCheckRepo;
import com.higgsup.kpi.repository.KpiMonthRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.LateTimeCheckService;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;
import com.higgsup.kpi.util.UtilsConvert;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LateTimeCheckServiceImpl implements LateTimeCheckService {
    @Autowired
    KpiMonthRepo kpiMonthRepo;

    @Autowired
    LdapUserService ldapUserService;

    @Autowired
    KpiUserRepo kpiUserRepo;

    @Autowired
    UserService userService;

    @Autowired
    KpiLateTimeCheckRepo kpiLateTimeCheckRepo;

    @Autowired
    private Environment environment;

    @Override
    @Transactional
    public List<KpiLateTimeCheck> createDataNewMonthOrUpdate() {
        KpiYearMonth kpiYearMonth = getAndCreateNewMonth();
        return createNewDataOrUpdateDate(kpiYearMonth);
    }

    @Override

    public List<LateTimeCheckDTO> getAllLateTimeCheckCurrent() {
        List<KpiLateTimeCheck> lateTimeChecksInDB = createDataNewMonthOrUpdate();
        return convertEntityLateTimeCheckToDTO(lateTimeChecksInDB);
    }

    @Override
    public LateTimeCheckDTO updateLateTimeCheckMonthCurrent(LateTimeCheckDTO lateTimeCheckDTO) {
        if (Objects.nonNull(lateTimeCheckDTO.getLateTimes())) {
            Optional<KpiLateTimeCheck> lateTimeCheckOptional = kpiLateTimeCheckRepo.findById(lateTimeCheckDTO.getId());
            if (lateTimeCheckOptional.isPresent()) {
                KpiLateTimeCheck lateTimeCheck = lateTimeCheckOptional.get();
                java.util.Date yearMonth = UtilsConvert.convertYearMonthIntToDate(lateTimeCheck.getYearMonth().getYearMonth());
                if (validateYearMonth(yearMonth)) {
                    lateTimeCheck.setLateTimes(lateTimeCheckDTO.getLateTimes());
                    lateTimeCheck = kpiLateTimeCheckRepo.save(lateTimeCheck);
                    lateTimeCheckDTO.setUser(convertUserDTOToEntity(lateTimeCheck.getUser()));
                } else {
                    lateTimeCheckDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    lateTimeCheckDTO.setMessage(ErrorMessage.ONLY_THE_CURRENT_MONTH_CAN_BE_EDITED);
                }

            } else {
                lateTimeCheckDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                lateTimeCheckDTO.setMessage(ErrorMessage.ID_NOT_INCORRECT);
            }
        } else {
            lateTimeCheckDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            lateTimeCheckDTO.setMessage(ErrorMessage.LATE_TIME_CAN_NOT_BE_EMPTY);
        }

        return lateTimeCheckDTO;
    }

    private boolean validateYearMonth(java.util.Date yearMonth) {
        //is true if in month old , is false if la new
        Timestamp dateCun = new Timestamp(System.currentTimeMillis());
        if (dateCun.getYear() + 1900 == yearMonth.getYear() + 1900
                && dateCun.getMonth() + 1 == yearMonth.getMonth() + 1
                && dateCun.getDate() >= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                ) {
            return true;
        } else if (dateCun.getYear() + 1900 == yearMonth.getYear() + 1900
                && dateCun.getMonth() + 1 > yearMonth.getMonth() + 1
                && (dateCun.getDate() <= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                || (dateCun.getDate() == Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                && dateCun.getHours() < Integer.valueOf(
                environment.getProperty("config.hour.new.year.month"))))) {
            return true;
        } else if (dateCun.getYear() + 1900 > yearMonth.getYear() + 1900 && (dateCun.getDate() <= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                || (dateCun.getDate() == Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                && dateCun.getHours() < Integer.valueOf(
                environment.getProperty("config.hour.new.year.month"))))) {
            return true;
        }
        return false;
    }

    private UserDTO convertUserDTOToEntity(KpiUser user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setUsername(user.getUserName());
        return userDTO;
    }

    private List<LateTimeCheckDTO> convertEntityLateTimeCheckToDTO(List<KpiLateTimeCheck> lateTimeChecksInDB) {
        List<LateTimeCheckDTO> lateTimeCheckDTOS = new ArrayList<>();
        for (KpiLateTimeCheck lateTimeCheck : lateTimeChecksInDB) {
            LateTimeCheckDTO lateTimeCheckDTO = new LateTimeCheckDTO();
            UserDTO user = new UserDTO();

            BeanUtils.copyProperties(lateTimeCheck, lateTimeCheckDTO, "user");
            BeanUtils.copyProperties(lateTimeCheck.getUser(), user, "avatar");
            user.setUsername(lateTimeCheck.getUser().getUserName());
            lateTimeCheckDTO.setUser(user);

            lateTimeCheckDTOS.add(lateTimeCheckDTO);
        }
        return lateTimeCheckDTOS;
    }

    private List<KpiLateTimeCheck> createNewDataOrUpdateDate(KpiYearMonth kpiYearMonth) {
        List<KpiLateTimeCheck> lateTimeChecksInDB = kpiLateTimeCheckRepo.findByMonth(kpiYearMonth);
        List<UserDTO> userDTOSInLdap = ldapUserService.getAllEmployeeUsers();
        List<UserDTO> userDTOSInLdapClone = new ArrayList<>(userDTOSInLdap);
        List<KpiUser> kpiUsersInDB = (List<KpiUser>) kpiUserRepo.findAll();

        userDTOSInLdap.removeIf(kpiUser -> kpiUsersInDB.stream().anyMatch(kpiUser1 -> Objects
                .equals(kpiUser1.getUserName(), kpiUser.getUsername())));

        userDTOSInLdap.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        List<KpiUser> kpiUsersAll = (List<KpiUser>) kpiUserRepo.findAll();

        //delete if other employee
        kpiUsersAll.removeIf(
                kpiUser -> userDTOSInLdapClone.stream()
                        .noneMatch(userDTO -> userDTO.getUsername().equals(kpiUser.getUserName())));
        //delete if has in LateTimeChecks
        kpiUsersAll.removeIf(
                kpiUser -> lateTimeChecksInDB.stream()
                        .anyMatch(userDTO -> userDTO.getUser().getUserName().equals(kpiUser.getUserName())));

        List<KpiLateTimeCheck> lateTimeChecksNew = new ArrayList<>();
        kpiUsersAll.forEach(kpiUser -> {
            KpiLateTimeCheck kpiLateTimeCheck = new KpiLateTimeCheck();
            kpiLateTimeCheck.setUser(kpiUser);
            kpiLateTimeCheck.setYearMonth(kpiYearMonth);
            lateTimeChecksNew.add(kpiLateTimeCheck);
        });
        List<KpiLateTimeCheck> lateTimeChecksNewSave = (List<KpiLateTimeCheck>) kpiLateTimeCheckRepo.saveAll(lateTimeChecksNew);
        lateTimeChecksInDB.addAll(lateTimeChecksNewSave);

        return lateTimeChecksInDB;
    }

    private KpiYearMonth getAndCreateNewMonth() {
        KpiYearMonth kpiYearMonth = null;
        Optional<KpiYearMonth> kpiMonthFromDB = kpiMonthRepo.findByMonthCurrent();
        if (kpiMonthFromDB.isPresent()) {
            kpiYearMonth = kpiMonthFromDB.get();
            Timestamp dateCun = new Timestamp(System.currentTimeMillis());
            java.util.Date date = UtilsConvert.convertYearMonthIntToDate(kpiYearMonth.getYearMonth());
            if (!validateYearMonth(date)) {
                KpiYearMonth kpiYearMonthCreate = new KpiYearMonth();
                kpiYearMonthCreate.setYearMonth(UtilsConvert.convertDateToYearMonthInt(dateCun));
                kpiYearMonth = kpiMonthRepo.save(kpiYearMonthCreate);
            }
        } else {
            java.util.Date dateCun = new Date(System.currentTimeMillis());
            KpiYearMonth kpiYearMonthCreate = new KpiYearMonth();
            kpiYearMonthCreate.setYearMonth(UtilsConvert.convertDateToYearMonthInt(dateCun));
            kpiYearMonth = kpiMonthRepo.save(kpiYearMonthCreate);
        }
        return kpiYearMonth;
    }
}
