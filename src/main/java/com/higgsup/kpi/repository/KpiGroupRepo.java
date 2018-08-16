package com.higgsup.kpi.repository;

import com.higgsup.kpi.dto.GroupTypeDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;

public interface KpiGroupRepo extends CrudRepository<KpiGroup, Integer> {
    KpiGroup findByName(String name);

    @Query(value = "SELECT * from kpi_group as g where g.group_type_id = :groupTypeId",nativeQuery = true)
    KpiGroup findByGroupTypeId(@Param("groupTypeId") Integer groupTypeId);

}
