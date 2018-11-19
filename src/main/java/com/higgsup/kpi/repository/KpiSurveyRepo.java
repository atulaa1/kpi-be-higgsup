package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KpiSurveyRepo extends CrudRepository<KpiSurvey, Integer> {
    @Query(value = "select * from kpi_survey_question_man where number=:number", nativeQuery = true)
    KpiSurvey findQuestionByNumber(@Param("number") Integer id);
}
