package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiProjectLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KpiProjectLogRepo extends CrudRepository<KpiProjectLog, Integer> {
    @Query(value = "SELECT count(DISTINCT man_username) from kpi_project_log as l where l.year_month = :yearMonth;",  nativeQuery = true)
    Integer countTheNumberOfManEvaluatingProject(@Param("yearMonth")Integer yearMonth);

    @Query(value = "SELECT count(DISTINCT project_id) from kpi_project_log as l where l.year_month = :yearMonth;",  nativeQuery = true)
    Integer countTheNumberOfProject(@Param("yearMonth")Integer yearMonth);
}
