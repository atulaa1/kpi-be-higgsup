package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiFamePoint;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiFamePointRepo extends CrudRepository< KpiFamePoint, Integer> {

    @Query(value = "select * from kpi_fame_point f where f.username = :username " +
            "and f.year = :year", nativeQuery = true)
    KpiFamePoint findByUsernameAndYear(@Param("year") Integer year, @Param("username") String username);

    @Query(value = "select * from kpi_fame_point f where f.username = :username order by f.year desc", nativeQuery = true)
    List<KpiFamePoint> findByUsername(@Param("username") String username);
}
