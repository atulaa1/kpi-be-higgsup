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

    @Query(value = "select distinct * from kpi_event as e " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "join kpi_group as g on g.id = e.group_id " +
            "where (eu.type = 1 and eu.user_name = :username) " +
            "or (g.group_type_id = 4 and eu.user_name = :username) " +
            "order by e.created_date desc, e.updated_date desc", nativeQuery = true)
    List<KpiEvent> findEventCreatedByUser(@Param("username") String username);

    @Query(value = "select * from kpi_event as e join kpi_group as g join kpi_group_type as t " +
            "where g.group_type_id = t.id and t.id = 1 and e.creator = :username and e.status = 2", nativeQuery = true)
    List<KpiEvent> findConfirmedClubEventCreatedByHost(@Param("username") String username);

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
            "and e.status = 2", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsMember(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 3 " +
            "and e.status = 2", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsListener(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 1 " +
            "and e.status = 2", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsHost(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 2 " +
            " and WEEKDAY(e.begin_date) = 5 and e.status = 2", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsHostAtSaturday(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "join kpi_event_user as eu on e.id = eu.event_id " +
            "where g.group_type_id = 1 and eu.user_name = :username and eu.type = 1 " +
            " and WEEKDAY(e.begin_date) = 5 and e.status = 2", nativeQuery = true)
    List<KpiEvent> findFinishedSurveySeminarEventByUserAsMemberAtSaturday(@Param("username") String username);

    @Query(value = "select e.* from kpi_event as e join kpi_group as g on g.id = e.group_id " +
            "where g.group_type_id = 1 and e.status = 1", nativeQuery = true)
    List<KpiEvent> findUnfinishedSurveySeminarEvent();
}
