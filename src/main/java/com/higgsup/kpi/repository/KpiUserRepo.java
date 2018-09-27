package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiUser;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KpiUserRepo extends CrudRepository<KpiUser, String>, KpiUserRepoCustom {
    KpiUser findByUserName(String username);

    KpiUser findByEmail(String email);
}
