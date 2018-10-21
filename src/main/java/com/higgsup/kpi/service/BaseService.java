package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiMonthRepo;
import com.higgsup.kpi.repository.KpiPointRepo;
import com.higgsup.kpi.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.higgsup.kpi.glossary.PointValue.MAX_CLUB_POINT;
import static com.higgsup.kpi.glossary.PointValue.MAX_SUPPORT_POINT;

public abstract class BaseService {

    @Autowired
    KpiPointRepo kpiPointRepo;

    @Autowired
    KpiEventRepo kpiEventRepo;

    @Autowired
    KpiMonthRepo kpiMonthRepo;

    @Autowired
    EventServiceImpl kpiEventService;

    @Autowired
    UserService userService;


    protected void addClubPoint(KpiUser kpiUser, Float point) {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            KpiPoint kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setClubPoint(point);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointRepo.save(kpiPoint);
        } else {
            KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            Float currentPoint = kpiPoint.getClubPoint();
            if (currentPoint < MAX_CLUB_POINT.getValue()) {
                if (MAX_CLUB_POINT.getValue() - currentPoint < point) {
                    kpiPoint.setClubPoint((float) MAX_CLUB_POINT.getValue());
                } else {
                    kpiPoint.setClubPoint(currentPoint + point);
                }
            }
            kpiPointRepo.save(kpiPoint);
        }
    }

    protected void addSupportPoint(KpiUser kpiUser, Float point) {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        KpiPoint kpiPoint = new KpiPoint();
        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            kpiPoint.setSupportPoint(point);
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointRepo.save(kpiPoint);
        } else {
            kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            if (kpiPoint.getSupportPoint() < MAX_SUPPORT_POINT.getValue()) {
                if (MAX_SUPPORT_POINT.getValue() - kpiPoint.getSupportPoint() < point) {
                    kpiPoint.setSupportPoint((float) MAX_SUPPORT_POINT.getValue());
                } else {
                    kpiPoint.setSupportPoint(kpiPoint.getSupportPoint() + point);
                }
            }
            kpiPointRepo.save(kpiPoint);
        }
    }
}


