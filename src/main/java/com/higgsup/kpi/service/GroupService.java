package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSeminarDetail;

public interface GroupService {
    GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException;
    GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException;
}
