package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPointDetail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiPointDetailRepo extends CrudRepository<KpiPointDetail, Integer> {

    @Query(value = "select * from kpi_point_detail p where p.user_name = :username and p.year_month_id = :id", nativeQuery = true)
    List<KpiPointDetail> findByUsernameAndYearMonthId(@Param("username") String username, @Param("id") Integer yearMonth);

}
