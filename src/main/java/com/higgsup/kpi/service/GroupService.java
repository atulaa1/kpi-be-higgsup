package com.higgsup.kpi.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSeminarDetail;
import com.higgsup.kpi.dto.GroupSupportDetail;

public interface GroupService {
    GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;
    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
}
