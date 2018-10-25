package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiYearMonth;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiMonthRepo extends CrudRepository<KpiYearMonth, Integer> {
    @Query(value = "SELECT * FROM kpi_year_month ORDER BY year_and_month DESC LIMIT 1", nativeQuery = true)
    Optional<KpiYearMonth> findByMonthCurrent();

    Optional<KpiYearMonth> findById(Integer id);

    @Query(value = "SELECT * FROM kpi_year_month ORDER BY year_and_month DESC LIMIT 1 OFFSET 1", nativeQuery = true)
    KpiYearMonth findByLastMonth();
}
