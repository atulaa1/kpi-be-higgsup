package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiLateTimeCheck;
import com.higgsup.kpi.entity.KpiYearMonth;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KpiLateTimeCheckRepo extends CrudRepository<KpiLateTimeCheck, Integer> {
    @Query(value = "SELECT g from KpiLateTimeCheck g where g.yearMonth=:yearMonth", nativeQuery = false)
    List<KpiLateTimeCheck> findByMonth(@Param("yearMonth") KpiYearMonth yearMonth);

    @Modifying
    @Transactional
    @Query(value = "DELETE from kpi_latetime_check where user_name=:username", nativeQuery = true)
    void deleteLateTimeCheck(@Param("username") String username);
}