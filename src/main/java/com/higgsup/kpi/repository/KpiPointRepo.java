package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiPointRepo extends CrudRepository<KpiPoint, Integer> {

    KpiPoint findByRatedUser(KpiUser kpiUser);

    @Query(value = "select * from kpi_point p join kpi_year_month y on p.year_month_id = y.id " +
            "where p.rated_username = :username order by y.year_and_month desc", nativeQuery = true)
    List<KpiPoint> findByRatedUsername(@Param("username") String username);

    @Query(value = "SELECT * FROM kpi_point ORDER BY total_point DESC, rated_username ASC LIMIT :limitRows", nativeQuery = true)
    List<KpiPoint> getNormalPointRanking(@Param("limitRows") Integer limitRows);

    @Query(value = "select * from kpi_point p where p.rated_username = :username" +
            " and p.year_month_id = :id", nativeQuery = true)
    KpiPoint findByRatedUsernameAndMonth(@Param("username") String username, @Param("id") Integer yearMonthId);

    @Query(value = "Select * from kpi_point p join kpi_year_month y on p.year_month_id = y.id " +
                   "where p.rated_username = :username and y.year_and_month between :l and :r " +
                   "order by y.year_and_month asc", nativeQuery = true)
    List<KpiPoint> getFamePointOfEmployeeInYear(@Param("username") String username,
                                                @Param("l") Integer left, @Param("r") Integer right);

    @Query(value = "select * from kpi_point p where p.year_month_id = :id", nativeQuery = true)
    List<KpiPoint> getAllPointInMonth(@Param("id")Integer yearMonthId);

    @Query(value = "select * from kpi_point as p join kpi_fame_point as f on p.rated_username = f.username " +
            "where p.total_point >= 120 and p.year_month_id = :id and f.year = :year " +
            "order by total_point desc, (p.rule_point + p.club_point + p.normal_seminar_point + p.weekend_seminar_point + p.support_point + p.teambuilding_point) desc, " +
            "f.fame_point desc limit 3", nativeQuery = true)
    List<KpiPoint> getTop3EmployeeInMonth(@Param("id") Integer yearMonthId, @Param("year") Integer fameYear);
}
