package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvaluation;
import com.higgsup.kpi.entity.KpiProjectLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiProjectLogRepo extends CrudRepository<KpiProjectLog, Integer> {
    @Query(value = "SELECT distinct(project_id) from kpi_project_log " +
            "join kpi_evaluation on evaluation_id = kpi_evaluation.id " +
            "where kpi_evaluation.year_month_id =:yearMonth", nativeQuery = true)
    List<Integer> getAllProjectEvaluated(@Param("yearMonth") Integer yearMonthId);

    @Query(value = "select sum(project_point) / count(distinct evaluation_id) from kpi_project_log " +
            "join kpi_evaluation on evaluation_id = kpi_evaluation.id "+
            "where project_id = :id and year_month_id = :yearMonth", nativeQuery = true)
    Float projectPoint(@Param("id") Integer projectId, @Param("yearMonth") Integer yearMonth);

    List<KpiProjectLog> findByEvaluation(KpiEvaluation kpiEvaluation);
}
