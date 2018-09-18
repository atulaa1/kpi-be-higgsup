package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiEventRepo extends CrudRepository<KpiEvent, Integer> {
    KpiEvent findByName(String name);

    @Query("SELECT e from KpiEvent e order by e.status asc, e.createdDate desc, e.updatedDate asc")
    List<KpiEvent> findAllEvent();

    @Query("SELECT e from KpiEvent e order by e.createdDate desc, e.updatedDate desc")
    List<KpiEvent> findEventCreatedByUser();
}
