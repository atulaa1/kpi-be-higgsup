package com.higgsup.kpi.service;

import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.repository.KpiPointRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import org.springframework.beans.factory.annotation.Autowired;

import static com.higgsup.kpi.glossary.PointValue.MAX_CLUB_POINT;

public abstract class BaseService {

    @Autowired
    KpiPointRepo kpiPointRepo;

    protected void addClubPoint(KpiUser kpiUser, Float point) {
        KpiPoint kpiPoint = new KpiPoint();
        kpiPoint.setRatedUser(kpiUser);

        if(kpiPointRepo.findByRatedUser(kpiUser) == null){
            kpiPoint.setClubPoint(point);
            kpiPointRepo.save(kpiPoint);
        }else{
            Float currentPoint = kpiPointRepo.findByRatedUser(kpiUser).getClubPoint();
            if(currentPoint < MAX_CLUB_POINT.getValue()){
                if(MAX_CLUB_POINT.getValue() - currentPoint < point){
                    kpiPoint.setClubPoint((float) MAX_CLUB_POINT.getValue());
                }else{
                    kpiPoint.setClubPoint(currentPoint + point);
                    kpiPointRepo.save(kpiPoint);
                }
            }
        }
    }

    protected void addSupportPoint(KpiUser kpiUser, Float point){
        KpiPoint kpiPoint = new KpiPoint();
        if(kpiPointRepo.findByRatedUser(kpiUser) == null){
            kpiPoint.setSupportPoint(point);
            kpiPoint.setRatedUser(kpiUser);
            kpiPointRepo.save(kpiPoint);
        }else{
            kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            kpiPoint.setSupportPoint(point + kpiPoint.getSupportPoint());
            kpiPointRepo.save(kpiPoint);
        }
    }
}
