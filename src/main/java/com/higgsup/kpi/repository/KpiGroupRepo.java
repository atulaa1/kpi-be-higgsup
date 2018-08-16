package com.higgsup.kpi.repository;

import com.higgsup.kpi.dto.GroupTypeDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaBuilder;

public interface KpiGroupRepo extends CrudRepository<KpiGroup, Integer> {
    KpiGroup findByName(String strName);

    @Query("select g from KpiGroup g where g.groupTypeId = :groupTypeId")
    KpiGroup findByGroupTypeId(@Param("groupTypeId") Integer groupTypeId);

}
