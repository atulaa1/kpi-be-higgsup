package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiEventUser;

import java.io.IOException;
import java.util.List;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventTeamBuildingDetail;

public interface PointService {
    void addSeminarPoint(List<KpiEventUser> eventUsers, EventDTO<EventSeminarDetail> seminarEventDTO) throws IOException;

    void calculateTeamBuildingPoint(EventDTO<EventTeamBuildingDetail> teamBuildingDTO);

    List<EmployeePointDetailDTO> getPointDetailByUser(String username) throws IOException;

    List<EmployeeFamePointDetailDTO> getFamePointOfEmployee(String username);

    List<PointDTO> getBestEmployeeOfMonths();
}

