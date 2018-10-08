package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.*;

public interface PointService {
    void calculateRulePoint();

    void calculateNormalSeminarPoint();

    void calculateTeambuildingPoint(EventDTO<TeamBuildingDTO> teamBuildingDTO);
}

