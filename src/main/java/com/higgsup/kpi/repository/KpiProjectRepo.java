package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiProject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiProjectRepo extends CrudRepository<KpiProject, Integer> {
    KpiProject findByName(String name);

    @Query(value = "SELECT * FROM kpi_project AS p ORDER BY active DESC, updated_date DESC", nativeQuery = true)
    List<KpiProject> findAllFollowCreateDateSorted();

    @Query(value = "SELECT * FROM kpi_project as p where p.active = 1 or MONTH(p.updated_date) = MONTH(CURRENT_DATE())", nativeQuery = true)
    List<KpiProject> findAllProjectsInMonth();
}
