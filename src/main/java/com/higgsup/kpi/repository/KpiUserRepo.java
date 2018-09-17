package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.repository.CrudRepository;

public interface KpiUserRepo extends CrudRepository<KpiUser, String>, KpiUserRepoCustom {
    KpiUser findByUserName(String username);

    KpiUser findByEmail(String email);
}
