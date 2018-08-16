package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;

public interface GroupService {
    GroupDTO createConfigTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException;

    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
}
