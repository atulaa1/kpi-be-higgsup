package com.higgsup.kpi.service;

import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.repository.KpiUserRepo;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseService {
    @Autowired
    KpiUserRepo kpiUserRepo;

    protected Float addPoint(String userName, Float point) {
        KpiUser kpiUser = kpiUserRepo.findByUserName(userName);
        return addPoint(kpiUser, point);
    }

    protected Float addPoint(KpiUser kpiUser, Float point) {
        return null;
    }
}
