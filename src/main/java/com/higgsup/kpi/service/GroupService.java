package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;

public interface GroupService {
    String createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;
}
