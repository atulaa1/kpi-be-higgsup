package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;

public interface GroupService {
    GroupDTO updateTeamBuildingActivity(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException;

    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
}
