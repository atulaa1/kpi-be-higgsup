package com.higgsup.kpi.repository;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.entity.KpiEvent;
import org.springframework.data.repository.CrudRepository;

public interface KpiEventRepo extends CrudRepository<KpiEvent, Integer> {
    EventDTO findByName(String name);

}
