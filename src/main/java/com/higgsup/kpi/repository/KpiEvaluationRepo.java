package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvaluation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiEvaluationRepo extends CrudRepository<KpiEvaluation, Integer> {
    @Query(value = "SELECT * from kpi_evaluation", nativeQuery = true)
    List<KpiEvaluation> findAllEvaluation();

    @Query(value = "SELECT count(DISTINCT man_username) from kpi_evaluation where year_month_id = :yearMonth", nativeQuery = true)
    Integer numberOfManCompleteEvaluation(@Param("yearMonth") Integer yearMonth);
}
