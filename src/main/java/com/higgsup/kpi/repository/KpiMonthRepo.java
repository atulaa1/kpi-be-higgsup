package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiMonth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface KpiMonthRepo extends CrudRepository<KpiMonth, Integer> {
    @Query(value = "SELECT * FROM kpi_month WHERE MONTH(month) = MONTH(CURRENT_DATE()) OR MONTH(month) = MONTH(CURRENT_DATE()) - 1 AND YEAR(month) = YEAR(CURRENT_DATE()) AND DATE_FORMAT( NOW(), '%H:%i' ) <= '16:00' AND Day(month) <= 10 ORDER BY month DESC LIMIT 1", nativeQuery = true)
    Optional<KpiMonth> findByMonthCurrent();
}
