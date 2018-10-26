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
    KpiPoint findByRatedUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM kpi_point ORDER BY total_point DESC, rated_username ASC  LIMIT :offset ,:limitRows", nativeQuery = true)
    List<KpiPoint> getNomalPointRanking(@Param("offset") Integer offset, @Param("limitRows") Integer limitRows);

    @Query(value = "SELECT * FROM kpi_point ORDER BY famed_point DESC, rated_username ASC  LIMIT :offset ,:limitRows", nativeQuery = true)
    List<KpiPoint> getFamedPointRanking(@Param("offset") Integer offset, @Param("limitRows") Integer limitRows);

    @Query(value = "SELECT * from kpi_point WHERE total_point = (SELECT MAX(total_point) from kpi_point where year_month_id =:yearMonthId)", nativeQuery = true)
    List<KpiPoint> getBestPerformancesOfMonth(@Param("yearMonthId") Integer yearMonthId);

    @Query(value = "SELECT DISTINCT year_month_id FROM kpi_point", nativeQuery = true)
    List<Integer> allOfYearMonthId();
}
