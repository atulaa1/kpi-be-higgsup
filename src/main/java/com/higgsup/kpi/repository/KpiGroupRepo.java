package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface KpiGroupRepo extends CrudRepository<KpiGroup, Integer> {
    KpiGroup findByName(String name);

    @Query(value = "SELECT * from kpi_group as g where g.group_type_id = :groupTypeId", nativeQuery = true)
    KpiGroup findGroupTypeId(@Param("groupTypeId") Integer groupTypeId);

    KpiGroup findByGroupTypeId(Optional<KpiGroupType> kpiGroupType);
}
