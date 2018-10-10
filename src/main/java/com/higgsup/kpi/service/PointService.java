package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiUser;

import java.io.IOException;
import java.util.List;

public interface PointService {
    void calculateRulePoint();

    void addSeminarPoint(List<KpiEventUser> eventUsers, EventDTO<EventSeminarDetail> seminarEventDTO) throws IOException;

    void calculateTeambuildingPoint(EventDTO<TeamBuildingDTO> teamBuildingDTO);

}

