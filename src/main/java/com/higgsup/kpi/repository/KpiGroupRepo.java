package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiGroupRepo extends CrudRepository<KpiGroup, Integer> {
    KpiGroup findByName(String name);
    KpiGroup findByGroupTypeId(Optional<KpiGroupType> kpiGroupType);
}
