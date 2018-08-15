package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Override
    public void getAllGroup() {
        List<KpiGroup> groupList = (List<KpiGroup>) kpiGroupRepo.findAll();
        convertGroupsEntityToDTO(groupList);
    }

    private void convertGroupsEntityToDTO(List<KpiGroup> groupList) {

        if (!CollectionUtils.isEmpty(groupList)) {
            for (KpiGroup kpiGroup : groupList) {
                switch (GroupType.getGroupType(kpiGroup.getGroupTypeId().getId())) {
                    case SEMINAR:
                        break;
                    case CLUB:
                        break;
                    case TEAM_BUILDING:
                        break;
                    case SUPPORT:
                        break;
                }
            }
        }
    }
}
