package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiGroupTypeRepo extends CrudRepository<KpiGroupType, Integer> {
    @Query("SELECT g from KpiGroupType g order by g.name")
    List<KpiGroupType> findAllGroupTypeOrderByNameASC();
}
