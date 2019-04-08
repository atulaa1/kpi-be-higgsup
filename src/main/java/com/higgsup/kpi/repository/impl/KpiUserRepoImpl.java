package com.higgsup.kpi.repository.impl;

import com.higgsup.kpi.repository.KpiUserRepoCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class KpiUserRepoImpl implements KpiUserRepoCustom {
    @PersistenceContext
    private EntityManager entityManager;
}
