package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiPointRepo extends CrudRepository<KpiPoint, Integer> {

    KpiPoint findByRatedUser(KpiUser kpiUser);

    @Query(value = "select * from kpi_point p where p.rated_username = :username", nativeQuery = true)
    List<KpiPoint> findByRatedUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM kpi_point ORDER BY total_point DESC, rated_username ASC LIMIT :limitRows", nativeQuery = true)
    List<KpiPoint> getNormalPointRanking(@Param("limitRows") Integer limitRows);

    @Query(value = "select * from kpi_point p where p.rated_username = :username" +
            " and p.year_month_id = :id", nativeQuery = true)
    KpiPoint findByRatedUsernameAndMonth(@Param("username") String username, @Param("id") Integer yearMonthId);
}
