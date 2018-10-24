package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.PointType;
import com.higgsup.kpi.repository.*;
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

    @Autowired
    KpiPointDetailRepo kpiPointDetailRepo;

    protected void addClubPoint(KpiUser kpiUser, Float point, KpiEvent event) {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            KpiPoint kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setClubPoint(point);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(event);
            kpiPointDetail.setPoint(point);
            kpiPointDetail.setPointType(PointType.CLUB_POINT.getValue());
            kpiPointDetail.setUser(kpiUser);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);

        } else {
            KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            Float currentPoint = kpiPoint.getClubPoint();
            Float addedPoint;
            if (currentPoint < MAX_CLUB_POINT.getValue()) {
                if (MAX_CLUB_POINT.getValue() - currentPoint < point) {
                    addedPoint = MAX_CLUB_POINT.getValue() - currentPoint;
                    kpiPoint.setClubPoint((float) MAX_CLUB_POINT.getValue());
                } else {
                    addedPoint = point;
                    kpiPoint.setClubPoint(currentPoint + point);
                }
                kpiPointRepo.save(kpiPoint);

                KpiPointDetail kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(event);
                kpiPointDetail.setPoint(addedPoint);
                kpiPointDetail.setPointType(PointType.CLUB_POINT.getValue());
                kpiPointDetail.setUser(kpiUser);
                kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }

    protected void addSupportPoint(KpiUser kpiUser, Float point, KpiEvent event) {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        Float addedPoint;
        KpiPoint kpiPoint = new KpiPoint();
        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            kpiPoint.setSupportPoint(point);
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(event);
            kpiPointDetail.setPoint(point);
            kpiPointDetail.setPointType(PointType.SUPPORT_POINT.getValue());
            kpiPointDetail.setUser(kpiUser);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);

        } else {
            kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            if (kpiPoint.getSupportPoint() < MAX_SUPPORT_POINT.getValue()) {
                if (MAX_SUPPORT_POINT.getValue() - kpiPoint.getSupportPoint() < point) {
                    addedPoint = MAX_SUPPORT_POINT.getValue() - kpiPoint.getSupportPoint();
                    kpiPoint.setSupportPoint((float)MAX_SUPPORT_POINT.getValue());
                } else {
                    addedPoint = point;
                    kpiPoint.setSupportPoint(kpiPoint.getSupportPoint() + addedPoint);
                }
                kpiPointRepo.save(kpiPoint);

                KpiPointDetail kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(event);
                kpiPointDetail.setPoint(addedPoint);
                kpiPointDetail.setPointType(PointType.SUPPORT_POINT.getValue());
                kpiPointDetail.setUser(kpiUser);
                kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }
}


