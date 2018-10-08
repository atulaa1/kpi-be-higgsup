package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiUser;

public interface PointService {
    void calculateRulePoint();

    void calculateSeminarPoint();

    void calculateTeambuildingPoint(EventDTO<TeamBuildingDTO> teamBuildingDTO);
}

