package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiPointRepo extends CrudRepository<KpiPoint, Integer> {

    KpiPoint findByRatedUser(KpiUser kpiUser);
}
