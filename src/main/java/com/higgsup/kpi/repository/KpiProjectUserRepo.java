package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiProjectUser;
import org.springframework.data.repository.CrudRepository;

public interface KpiProjectUserRepo extends CrudRepository<KpiProjectUser, Integer> {
    void deleteByProjectId(Integer projectId);
}
