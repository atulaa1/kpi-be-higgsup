package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiSeminarSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiSeminarSurveyRepo extends CrudRepository<KpiSeminarSurvey, Integer> {
    @Query(value = "select * from kpi_event_user as eu where eu.event_id = :eventId and eu.type = :userType ", nativeQuery = true)
    List<KpiEventUser> findByUserType(@Param("userType") Integer userType, @Param("eventId") Integer eventId);
}
