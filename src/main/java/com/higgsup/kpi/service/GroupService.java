package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;

public interface GroupService {
    void create(GroupDTO<TeamBuildingDTO> groupDTO)  throws JsonProcessingException;
}
