package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.entity.KpiGroup;

public interface GroupService {
    void create(GroupDTO groupDTO);
}
