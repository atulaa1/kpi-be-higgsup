package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiGroupTypeRepo extends CrudRepository<KpiGroupType, Integer> {
    @Query("SELECT g from KpiGroupType g order by g.name")
    List<KpiGroupType> findAllGroupTypeOrderByNameASC();

    @Query(value = "SELECT * from kpi_group_type g join kpi_group r where r.group_type_id = g.id and r.id = :id", nativeQuery = true)
    KpiGroupType findByGroupId(@Param("id") Integer groupId);
}
