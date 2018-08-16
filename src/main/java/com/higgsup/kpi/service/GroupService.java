package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;

public interface GroupService {
    GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;

public interface GroupService {
    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
}
