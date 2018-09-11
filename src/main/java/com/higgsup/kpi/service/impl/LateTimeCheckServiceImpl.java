package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiLateTimeCheck;
import com.higgsup.kpi.entity.KpiMonth;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.repository.KpiLateTimeCheckRepo;
import com.higgsup.kpi.repository.KpiMonthRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.LateTimeCheckService;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    @Transactional
    public List<KpiLateTimeCheck> createDataNewMonthOrUpdate() {
        KpiMonth kpiMonth = getAndCreateNewMonth();
        return createNewDataOrUpdateDate(kpiMonth);
    }

    private List<KpiLateTimeCheck> createNewDataOrUpdateDate(KpiMonth kpiMonth) {
        List<KpiLateTimeCheck> lateTimeChecksInDB = kpiLateTimeCheckRepo.findByMonth(kpiMonth);
        List<UserDTO> userDTOSInLdap = ldapUserService.getAllEmployeeUsers();
        List<UserDTO> userDTOSInLdapClone = new ArrayList<>(userDTOSInLdap);
        List<KpiUser> kpiUsersInDB = (List<KpiUser>) kpiUserRepo.findAll();

        userDTOSInLdap.removeIf(kpiUser -> kpiUsersInDB.stream().anyMatch(kpiUser1 -> Objects
                .equals(kpiUser1.getUserName(), kpiUser.getUsername())));

        userDTOSInLdap.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        List<KpiUser> kpiUsersALL = (List<KpiUser>) kpiUserRepo.findAll();

        //delete if other employee
        kpiUsersALL.removeIf(
                kpiUser -> userDTOSInLdapClone.stream()
                        .noneMatch(userDTO -> userDTO.getUsername().equals(kpiUser.getUserName())));
        //delete if has in LateTimeChecks
        kpiUsersALL.removeIf(
                kpiUser -> lateTimeChecksInDB.stream()
                        .anyMatch(userDTO -> userDTO.getUser().getUserName().equals(kpiUser.getUserName())));

        List<KpiLateTimeCheck> lateTimeChecksNew = new ArrayList<>();
        kpiUsersALL.forEach(kpiUser -> {
            KpiLateTimeCheck kpiLateTimeCheck = new KpiLateTimeCheck();
            kpiLateTimeCheck.setUser(kpiUser);
            kpiLateTimeCheck.setMonth(kpiMonth);
            lateTimeChecksNew.add(kpiLateTimeCheck);
        });
        List<KpiLateTimeCheck> lateTimeChecksNewSave = (List<KpiLateTimeCheck>) kpiLateTimeCheckRepo.saveAll(lateTimeChecksNew);
        lateTimeChecksInDB.addAll(lateTimeChecksNewSave);

        return lateTimeChecksInDB;
    }

    private KpiMonth getAndCreateNewMonth() {
        KpiMonth kpiMonth = null;
        Optional<KpiMonth> kpiMonthFromDB = kpiMonthRepo.findByMonthCurrent();
        if (kpiMonthFromDB.isPresent()) {
            kpiMonth = kpiMonthFromDB.get();
        } else {
            KpiMonth kpiMonthCreate = new KpiMonth();
            kpiMonth = kpiMonthRepo.save(kpiMonthCreate);
        }
        return kpiMonth;
    }
}