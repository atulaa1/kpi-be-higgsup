package com.higgsup.kpi.service;

import com.higgsup.kpi.entity.KpiLateTimeCheck;

import java.util.List;

public interface LateTimeCheckService {
    List<KpiLateTimeCheck> createDataNewMonthOrUpdate();
}
