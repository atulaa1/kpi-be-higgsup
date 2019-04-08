package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPersonalSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiPersonalSurveyRepo extends CrudRepository<KpiPersonalSurvey, Integer> {
    @Query(value = "SELECT distinct(rated_username) from kpi_personal_survey " +
            "join kpi_evaluation on evaluation_id = kpi_evaluation.id " +
            "where kpi_evaluation.year_month_id =:yearMonth", nativeQuery = true)
    List<String> evaluatedList(@Param("yearMonth") Integer yearMonthId);

    @Query(value = "SELECT sum(personal_point) / count(distinct evaluation_id) from kpi_personal_survey " +
            "join kpi_evaluation on evaluation_id = kpi_evaluation.id" +
            " where rated_username = :username and year_month_id = :yearMonth", nativeQuery = true)
    Float point(@Param("username") String username, @Param("yearMonth") Integer yearMonthId);

    @Query(value = "SELECT * from kpi_personal_survey where rated_username = :username and evaluation_id=:id", nativeQuery = true)
    List<KpiPersonalSurvey> findByUsername(@Param("username") String username, @Param("id") Integer evaluationId);

    @Query(value = "SELECT distinct (rated_username) from kpi_personal_survey where evaluation_id=:id", nativeQuery = true)
    List<String> ratedUsername(@Param("id") Integer evaluationId);
}
