package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiProjectUser;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface KpiProjectUserRepo extends CrudRepository<KpiProjectUser, Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM kpi_project_user WHERE project_id=:projectId", nativeQuery = true)
    void deleteByProjectId(@Param("projectId") Integer projectId);

    @Query(value = "SELECT rated_username from kpi_project_user where project_id=:projectId", nativeQuery = true)
    List<String> getAllUsernameInProject(@Param("projectId") Integer projectId);

    @Query(value = "select count(project_id) from kpi_project_user where rated_username=:name", nativeQuery = true)
    Integer projectParticipate(@Param("name") String username);
}
