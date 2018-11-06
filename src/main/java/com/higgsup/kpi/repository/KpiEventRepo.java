package com.higgsup.kpi.repository;

import com.higgsup.kpi.entity.KpiEvent;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface KpiEventRepo extends CrudRepository<KpiEvent, Integer> {
    KpiEvent findByName(String name);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "where g.group_type_id = 2 or g.group_type_id = 4 " +
            "order by e.status asc, e.created_date desc, e.updated_date asc", nativeQuery = true)
    List<KpiEvent> findClubAndSupportEvent();

    @Query(value = "SELECT DISTINCT * FROM kpi_event AS e" +
            " JOIN kpi_event_user AS eu ON e.id = eu.event_id JOIN kpi_group AS g ON g.id = e.group_id " +
            "WHERE (eu.type = 1 AND eu.user_name = :username) OR (g.group_type_id = 4 AND eu.user_name = :username) " +
            "or (g.group_type_id = 2 && position(:username   in g.additional_config)) " +
            "GROUP BY e.id " +
            "ORDER BY e.created_date DESC, e.updated_date DESC", nativeQuery = true)
    List<KpiEvent> findEventCreatedByUser(@Param("username") String username);

    @Query(value = "select * from kpi_event as e join kpi_group as g on e.group_id = g.id " +
            " where e.status = 2 and g.id = :id " +
            " and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findConfirmedClubEventInMonth(@Param("id") Integer clubId, @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username " +
            "order by MONTH(e.created_date) desc, eu.status asc", nativeQuery = true)
    List<KpiEvent> findSeminarEventByUser(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id" +
            " where g.group_type_id = 3 order by e.created_date desc", nativeQuery = true)
    List<KpiEvent> findTeamBuildingEvent();

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 2 " +
            "and e.status = 2 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsMember(@Param("username") String username,
                                                                @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 3 " +
            "and e.status = 2 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsListener(@Param("username") String username,
                                                                  @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 1 " +
            "and e.status = 2 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsHost(@Param("username") String username,
                                                              @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 2 " +
            " and WEEKDAY(e.begin_date) = 5 and e.status = 2 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsHostAtSaturday(@Param("username") String username,
                                                                        @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 1 " +
            " and WEEKDAY(e.begin_date) = 5 and e.status = 2 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsMemberAtSaturday(@Param("username") String username,
                                                                          @Param("m") Integer month);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "where g.group_type_id = 1 and e.status = 1 and MONTH(e.created_date) = :m", nativeQuery = true)
    List<KpiEvent> findUnfinishedSurveySeminarEventInMonth(@Param("m") Integer month);
}
