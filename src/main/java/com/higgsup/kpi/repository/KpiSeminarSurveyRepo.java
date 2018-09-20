package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiSeminarSurvey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiSeminarSurveyRepo extends CrudRepository<KpiSeminarSurvey, Integer> {
    @Query("select s from KpiEventUser where s.event_id = :eventID and s.type = :userType ")
    List<KpiEventUser> findByUserType(@Param("userType") Integer userType, @Param("eventId") Integer eventId);
}
