package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KpiGroupRepo extends CrudRepository<KpiGroup, Integer> {
    KpiGroup findByName(String name);

    @Query(value = "SELECT * from kpi_group as g where g.group_type_id = :groupTypeId", nativeQuery = true)
    KpiGroup findGroupTypeId(@Param("groupTypeId") Integer groupTypeId);

    KpiGroup findByGroupType(Optional<KpiGroupType> kpiGroupType);

    @Query("SELECT g from KpiGroup g order by g.createdDate desc")
    List<KpiGroup> findAllGroup();

    @Query(value = "SELECT * from kpi_group as g where g.group_type_id = 2", nativeQuery = true)
    List<KpiGroup> findAllClub();

    @Query(value = "SELECT * from kpi_group as g join kpi_event e where e.group_id = g.id and e.id = :id", nativeQuery = true)
    KpiGroup findGroupByEventId(@Param("id") Integer eventId);
}
