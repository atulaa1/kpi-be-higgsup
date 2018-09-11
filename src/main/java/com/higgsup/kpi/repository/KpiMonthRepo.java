package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiMonth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiMonthRepo extends CrudRepository<KpiMonth, Integer> {
    @Query(value = "SELECT * FROM kpi_month WHERE MONTH(month) = MONTH(CURRENT_DATE()) AND YEAR(month) = YEAR(CURRENT_DATE()) LIMIT 1", nativeQuery = true)
    Optional<KpiMonth> findByMonthCurrent();
}
