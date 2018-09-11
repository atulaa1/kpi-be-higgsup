package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiLateTimeCheck;
import com.higgsup.kpi.entity.KpiMonth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiLateTimeCheckRepo extends CrudRepository<KpiLateTimeCheck, Integer> {
    @Query(value = "SELECT g from KpiLateTimeCheck g where g.month=:month", nativeQuery = false)
    List<KpiLateTimeCheck> findByMonth(@Param("month") KpiMonth month);
}
