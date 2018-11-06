package com.higgsup.kpi.service;

import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.PointType;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
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
        Optional<KpiYearMonth> kpiYearMonth;
        if(LocalDate.now().getMonthValue() == event.getCreatedDate().getMonth() + 1){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        }
        KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(kpiUser.getUserName(), kpiYearMonth.get().getId());
        if (kpiPoint == null) {
            kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setClubPoint(point);
            kpiPoint.setTotalPoint(point);
            kpiPoint.setYearMonthId(kpiYearMonth.get().getId());
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(event);
            kpiPointDetail.setPoint(point);
            kpiPointDetail.setPointType(PointType.CLUB_POINT.getValue());
            kpiPointDetail.setUser(kpiUser);
            kpiPointDetail.setYearMonthId(kpiYearMonth.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);

        } else {
            Float remainingPoint = MAX_CLUB_POINT.getValue() - kpiPoint.getClubPoint();
            Float addedPoint;
            if (remainingPoint > 0) {
                if (remainingPoint < point) {
                    addedPoint = remainingPoint;
                    kpiPoint.setClubPoint((float) MAX_CLUB_POINT.getValue());
                } else {
                    addedPoint = point;
                    kpiPoint.setClubPoint(kpiPoint.getClubPoint() + point);
                }
                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + addedPoint);
                kpiPointRepo.save(kpiPoint);

                KpiPointDetail kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(event);
                kpiPointDetail.setPoint(addedPoint);
                kpiPointDetail.setPointType(PointType.CLUB_POINT.getValue());
                kpiPointDetail.setUser(kpiUser);
                kpiPointDetail.setYearMonthId(kpiYearMonth.get().getId());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }

    protected void addSupportPoint(KpiUser kpiUser, Float point, KpiEvent event) {
        Optional<KpiYearMonth> kpiYearMonth;
        if(LocalDate.now().getMonthValue() == event.getCreatedDate().getMonth() + 1){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        }
        KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(kpiUser.getUserName(), kpiYearMonth.get().getId());
        Float addedPoint;
        if (kpiPoint == null) {
            kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUser);
            kpiPoint.setSupportPoint(point);
            kpiPoint.setTotalPoint(point);
            kpiPoint.setYearMonthId(kpiYearMonth.get().getId());
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(event);
            kpiPointDetail.setPoint(point);
            kpiPointDetail.setPointType(PointType.SUPPORT_POINT.getValue());
            kpiPointDetail.setUser(kpiUser);
            kpiPointDetail.setYearMonthId(kpiYearMonth.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);

        } else {
            Float remainingPoint = MAX_SUPPORT_POINT.getValue() - kpiPoint.getSupportPoint();
            if (remainingPoint > 0) {
                if (remainingPoint < point) {
                    addedPoint = remainingPoint;
                    kpiPoint.setSupportPoint((float)MAX_SUPPORT_POINT.getValue());
                } else {
                    addedPoint = point;
                    kpiPoint.setSupportPoint(kpiPoint.getSupportPoint() + addedPoint);
                }
                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + addedPoint);
                kpiPointRepo.save(kpiPoint);

                KpiPointDetail kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(event);
                kpiPointDetail.setPoint(addedPoint);
                kpiPointDetail.setPointType(PointType.SUPPORT_POINT.getValue());
                kpiPointDetail.setUser(kpiUser);
                kpiPointDetail.setYearMonthId(kpiYearMonth.get().getId());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }
}


