package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPersonalSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface KpiPersonalSurveyRepo extends CrudRepository<KpiPersonalSurvey, Integer> {
    @Query(value = "SELECT count(DISTINCT man_username) from kpi_personal_survey as p where p.year_month = :yearMonth;",  nativeQuery = true)
    Integer countTheNumberOfManEvaluatingEmployee(@Param("yearMonth")Integer yearMonth);
}
