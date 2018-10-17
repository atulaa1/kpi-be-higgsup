package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPersonalEvaluation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KpiPersonalEvaluationRepo extends CrudRepository<KpiPersonalEvaluation, String> {
    @Query(value = "select count (*) from kpi_personal_evaluation as e where e.survey_name = :surveyName ", nativeQuery = true)
    Integer countEvaluator(@Param("surveyName") String surveyName);

}
