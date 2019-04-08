package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiSurveyRepo extends CrudRepository<KpiSurvey, Integer> {
    @Query(value = "select * from kpi_survey_question_man", nativeQuery = true)
    List<KpiSurvey> findQuestion();
}
