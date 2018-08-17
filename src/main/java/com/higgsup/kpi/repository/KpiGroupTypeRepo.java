package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiGroupType;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiGroupTypeRepo extends CrudRepository<KpiGroupType, Integer> {

}
