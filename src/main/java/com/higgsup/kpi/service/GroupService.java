package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;

public interface GroupService {
    GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException;
}
