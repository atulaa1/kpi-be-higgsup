package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiProject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface KpiProjectRepo extends CrudRepository<KpiProject, Integer> {
    KpiProject findByName(String name);

    @Query(value = "SELECT * from kpi_project as p order by created_date DESC", nativeQuery = true)
    List<KpiProject> findAllFollowCreateDateSorted();
}
