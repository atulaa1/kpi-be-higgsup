package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.ErrorDTO;
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
import com.higgsup.kpi.util.UtilsValidate;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

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
        List<LateTimeCheckDTO> lateTimeCheckDTOS = convertEntityLateTimeCheckToDTO(lateTimeChecksInDB);
        lateTimeCheckDTOS.sort(Comparator.comparing(o -> o.getUser().getFullName()));
        return lateTimeCheckDTOS;
    }

    @Override
    public List<LateTimeCheckDTO> processExcelFile(MultipartFile excelDataFile) throws IOException {
        List<LateTimeCheckDTO> lateTimeCheckDTOS = new ArrayList<>();
        if (validateExcelFile(excelDataFile).isEmpty()) {
            XSSFWorkbook workbook = new XSSFWorkbook(excelDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            KpiYearMonth kpiYearMonth = getAndCreateNewMonth();
            List<KpiLateTimeCheck> lateTimeChecksInDB = kpiLateTimeCheckRepo.findByMonth(kpiYearMonth);
            List<KpiLateTimeCheck> kpiLateTimeChecks = new ArrayList<>();

            for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                LateTimeCheckDTO lateTimeCheckDTO = new LateTimeCheckDTO();
                UserDTO userDTO = new UserDTO();
                XSSFRow row = worksheet.getRow(i);

                userDTO.setFullName(row.getCell(0).getStringCellValue());
                userDTO.setEmail(row.getCell(1).getStringCellValue());

                lateTimeCheckDTO.setLateTimes((int) row.getCell(2).getNumericCellValue());
                lateTimeCheckDTO.setUser(userDTO);

                Optional<KpiLateTimeCheck> timeCheckOptional = lateTimeChecksInDB.stream().filter(
                        kpiLateTimeCheck1 -> kpiLateTimeCheck1.getUser().getEmail().equals(lateTimeCheckDTO.getUser().getEmail()))
                        .findFirst();
                if (timeCheckOptional.isPresent()) {
                    KpiLateTimeCheck kpiLateTimeCheck = timeCheckOptional.get();
                    kpiLateTimeCheck.setLateTimes(lateTimeCheckDTO.getLateTimes());
                    kpiLateTimeChecks.add(kpiLateTimeCheck);
                    lateTimeCheckDTOS.add(lateTimeCheckDTO);
                }
            }
            Iterable<KpiLateTimeCheck> kpiLateTimeChecks1 = kpiLateTimeCheckRepo.saveAll(kpiLateTimeChecks);
            return convertEntityLateTimeCheckToDTO((List<KpiLateTimeCheck>) kpiLateTimeChecks1);

        } else {
            LateTimeCheckDTO lateTimeCheckDTO = new LateTimeCheckDTO();
            lateTimeCheckDTO.setErrorDTOS(validateExcelFile(excelDataFile));
            lateTimeCheckDTOS.add(lateTimeCheckDTO);
        }
        return lateTimeCheckDTOS;
    }

    @Override
    public LateTimeCheckDTO updateLateTimesOfCurrentMonth(LateTimeCheckDTO lateTimeCheckDTO) {
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
        Timestamp dateCurrent = new Timestamp(System.currentTimeMillis());
        if ((checkDateAndMoth())) {
            return true;
        }
        if (dateCurrent.getYear() + 1900 == yearMonth.getYear() + 1900
                && dateCurrent.getMonth() + 1 == yearMonth.getMonth() + 1
                && dateCurrent.getDate() >= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                ) {
            return true;
        } else if (dateCurrent.getYear() + 1900 == yearMonth.getYear() + 1900
                && dateCurrent.getMonth() + 1 > yearMonth.getMonth() + 1
                && (dateCurrent.getDate() <= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                || (dateCurrent.getDate() == Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                && dateCurrent.getHours() < Integer.valueOf(
                environment.getProperty("config.hour.new.year.month"))))) {
            return true;
        } else if (dateCurrent.getYear() + 1900 > yearMonth.getYear() + 1900 && (dateCurrent.getDate() <= Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                || (dateCurrent.getDate() == Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                && dateCurrent.getHours() < Integer.valueOf(
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

        List<UserDTO> userDTOSInLdap = ldapUserService.getAllEmployeeAndManUsers();
        // delete user is man
        List<KpiLateTimeCheck> listManForDelete;
        List<UserDTO> listUserMan = userDTOSInLdap.parallelStream().filter(userDTO -> userDTO.getUserRole().indexOf("ROLE_MAN")!=-1).collect(Collectors.toList());
        listManForDelete = lateTimeChecksInDB.parallelStream().filter(kpiLateTimeCheck -> listUserMan.parallelStream().anyMatch(userDTO -> userDTO.getUsername().equals(kpiLateTimeCheck.getUser().getUserName()))).collect(Collectors.toList());
        kpiLateTimeCheckRepo.deleteAll(listManForDelete);
        lateTimeChecksInDB.removeIf(kpiLateTimeCheck -> listManForDelete.stream().anyMatch(kpiLateTimeCheck1 -> kpiLateTimeCheck1.getUser().getUserName().equals(kpiLateTimeCheck.getUser().getUserName())));
        // end delete user is man
        
        List<KpiUser> kpiUsersInDB = (List<KpiUser>) kpiUserRepo.findAll();
        List<UserDTO> userDTOSInLdapClone = new ArrayList<>(userDTOSInLdap);
        //delete when has user in db
        userDTOSInLdap.removeIf(kpiUser -> kpiUsersInDB.stream().anyMatch(kpiUser1 -> Objects
                .equals(kpiUser1.getUserName(), kpiUser.getUsername())));
        //registerUser if not has
        userDTOSInLdap.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        // get all Employee
        List<UserDTO> userDTOEmployeesInLdap = userDTOSInLdapClone.stream().filter(userDTO -> userDTO.getUserRole().size() == 1)
                .collect(
                        Collectors.toList());

        //get new after update
        List<KpiUser> kpiUsersAll = (List<KpiUser>) kpiUserRepo.findAll();

        //delete if other employee
        kpiUsersAll.removeIf(
                kpiUser -> userDTOEmployeesInLdap.stream()
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
        Timestamp dateCun = new Timestamp(System.currentTimeMillis());
        KpiYearMonth kpiYearMonth = null;
        if ((checkDateAndMoth())) {
            dateCun.setMonth(dateCun.getMonth() - 1);
        }
        kpiYearMonth = kpiMonthRepo.findByYearMonth(UtilsConvert.convertDateToYearMonthInt(dateCun));
        if (Objects.isNull(kpiYearMonth)) {
            KpiYearMonth kpiYearMonthCreate = new KpiYearMonth();
            kpiYearMonthCreate.setYearMonth(UtilsConvert.convertDateToYearMonthInt(dateCun));
            return kpiMonthRepo.save(kpiYearMonthCreate);
        }
        return kpiYearMonth;
    }

    private boolean checkDateAndMoth() {
        Timestamp dateCurrent = new Timestamp(System.currentTimeMillis());
        return (dateCurrent.getDate() < 10) ||
                (dateCurrent.getDate() == 10 && dateCurrent.getHours() < Integer.valueOf(
                        environment.getProperty("config.hour.new.year.month")));
    }

    private List<ErrorDTO> validateExcelFile(MultipartFile excelDataFile) throws IOException {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if (!excelDataFile.getOriginalFilename().endsWith(".xlsx")) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.INCORRECT_FILE_FORMAT.getValue());
            errorDTO.setMessage(ErrorMessage.INCORRECT_FILE_FORMAT);
            errorDTOS.add(errorDTO);
        } else {
            XSSFWorkbook workbook = new XSSFWorkbook(excelDataFile.getInputStream());
            XSSFSheet worksheet = workbook.getSheetAt(0);
            XSSFRow titleRow = worksheet.getRow(0);
            if (titleRow.getCell(0) == null ||
                    !titleRow.getCell(0).getStringCellValue().trim().toLowerCase().equals("team member")) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.INVALID_COLUMN_NAME.getValue());
                errorDTO.setMessage(ErrorMessage.INVALID_MEMBER_NAME);
                errorDTOS.add(errorDTO);
            } else if (titleRow.getCell(1) == null ||
                    !titleRow.getCell(1).getStringCellValue().trim().toLowerCase().equals("email")) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.INVALID_COLUMN_NAME.getValue());
                errorDTO.setMessage(ErrorMessage.INVALID_EMAIL);
                errorDTOS.add(errorDTO);
            } else if (titleRow.getCell(2) == null ||
                    !titleRow.getCell(2).getStringCellValue().trim().toLowerCase().equals("score")) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.INVALID_COLUMN_NAME.getValue());
                errorDTO.setMessage(ErrorMessage.INVALID_NUMBER_OF_LATE_TIMES);
                errorDTOS.add(errorDTO);
            }

            if (errorDTOS.isEmpty()) {
                for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
                    XSSFRow row = worksheet.getRow(i);
                    if (row.getCell(1) == null ||
                            !row.getCell(1).getStringCellValue().endsWith("@higgsup.com")) {
                        ErrorDTO errorDTO = new ErrorDTO();
                        errorDTO.setErrorCode(ErrorCode.INCORRECT_DATA.getValue());
                        errorDTO.setMessage(ErrorMessage.INCORRECT_EMAIL_DATA + (i + 1));
                        errorDTOS.add(errorDTO);
                    } else {
                        if (kpiUserRepo.findByEmail(row.getCell(1).getStringCellValue()) == null) {
                            ErrorDTO errorDTO = new ErrorDTO();
                            errorDTO.setErrorCode(ErrorCode.INCORRECT_DATA.getValue());
                            errorDTO.setMessage(ErrorMessage.EMAIL_NOT_IN_DATABASE + (i + 1));
                            errorDTOS.add(errorDTO);
                        }
                    }
                    if (row.getCell(2) == null ||
                            !UtilsValidate.isValidLateTimeNumber((row.getCell(2).toString()))) {
                        ErrorDTO errorDTO = new ErrorDTO();
                        errorDTO.setErrorCode(ErrorCode.INCORRECT_DATA.getValue());
                        errorDTO.setMessage(ErrorMessage.INCORRECT_LATE_TIMES_DATA + (i + 1));
                        errorDTOS.add(errorDTO);
                    }
                }
            }
        }
        return errorDTOS;
    }
}
