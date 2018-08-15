package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiUser;

public interface UserRepository {
    public KpiUser findByUserName(String username);
}
