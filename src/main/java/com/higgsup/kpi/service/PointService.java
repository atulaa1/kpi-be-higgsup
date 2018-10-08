package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventTeamBuildingDetail;

public interface PointService {
    void calculateRulePoint();

    void calculateNormalSeminarPoint();

    void calculateTeambuildingPoint(EventDTO<EventTeamBuildingDetail> teamBuildingDTO);
}

