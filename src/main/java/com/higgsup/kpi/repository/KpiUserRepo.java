package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiProject;
import com.higgsup.kpi.entity.KpiUser;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiUserRepo extends CrudRepository<KpiUser, String>, KpiUserRepoCustom {
    KpiUser findByUserName(String username);

    KpiUser findByEmail(String email);

    @Query(value = "SELECT u.avatar_url FROM kpi_user AS  u WHERE u.user_name=:username", nativeQuery = true)
    String findAvatar(@Param("username") String username);

    @Query(value = "SELECT u.full_name FROM kpi_user AS  u WHERE u.user_name=:username", nativeQuery = true)
    String findFullName(@Param("username") String username);

}
