package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.*;

import java.io.IOException;
import java.util.List;

public interface GroupService {
    GroupDTO updateTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException;

    GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;

    GroupDTO createSupportNew(GroupDTO<List<SupportDTO>> groupDTO) throws JsonProcessingException;

    SupportDTO createTaskSupport(SupportDTO groupDTO) throws JsonProcessingException;

    SupportDTO updateTaskSupport(SupportDTO supportDTO) throws JsonProcessingException;

    GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;

    GroupDTO createTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException;

    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;

    GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException;

    GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;

    GroupDTO createSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException;

    List<GroupDTO> getAllGroup() throws IOException;

    List<GroupDTO> getAllGroupNewSupport() throws IOException;

    List<GroupDTO> getGroupCanCreateEvent(String username) throws IOException;
}
