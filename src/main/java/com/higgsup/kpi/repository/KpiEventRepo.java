package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiEventRepo extends CrudRepository<KpiEvent, Integer> {
    KpiEvent findByName(String name);

    @Query("SELECT e from KpiEvent e order by e.status asc, e.createdDate desc, e.updatedDate asc")
    List<KpiEvent> findAllEvent();

    @Query(value = "select distinct * from kpi_event as e " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "join kpi_group as g on g.id = e.group_id " +
            "where (eu.type = 1 and eu.user_name = :username) " +
            "or (g.group_type_id = 4 and eu.user_name = :username) " +
            "order by e.created_date desc, e.updated_date desc", nativeQuery = true)
    List<KpiEvent> findEventCreatedByUser(@Param("username") String username);
}
