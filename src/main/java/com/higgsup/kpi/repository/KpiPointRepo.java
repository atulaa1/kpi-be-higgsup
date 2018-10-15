package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiPointRepo extends CrudRepository<KpiPoint, Integer> {

    KpiPoint findByRatedUser(KpiUser kpiUser);

    @Query(value = "select * from kpi_point p where p.rated_username = :username", nativeQuery = true)
    KpiPoint findByRatedUsername(@Param("username")String username);
}