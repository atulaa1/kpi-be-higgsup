package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.repository.CrudRepository;

public interface KpiGroupRepo extends CrudRepository<KpiGroupType, Integer> {
}
