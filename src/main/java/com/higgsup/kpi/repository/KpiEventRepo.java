package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.io.IOException;
import java.util.List;

public interface KpiEventRepo extends CrudRepository<KpiEvent, Integer> {
    KpiEvent findByName(String name);

    @Query("SELECT e from KpiEvent e order by e.status asc, e.createdDate desc, e.updatedDate asc")
    List<KpiEvent> findAllEvent();

    @Query(value = "select * from kpi_event as e join kpi_group as g on g.id = e.group_id" +
            " where g.group_type_id = 3 order by e.created_date desc", nativeQuery = true)
    List<KpiEvent> findTeamBuildingEvent();
}
