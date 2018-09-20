package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiSupport;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiSupportRepo extends CrudRepository<KpiSupport, Integer> {
    Optional<KpiSupport> findByTaskName(String taskName);
}
