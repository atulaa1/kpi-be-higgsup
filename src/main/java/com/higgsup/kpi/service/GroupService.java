package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;
import com.higgsup.kpi.dto.GroupClubDetail;
public interface GroupService {
    GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;
    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
}
