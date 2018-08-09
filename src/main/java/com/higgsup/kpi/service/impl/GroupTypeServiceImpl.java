package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupTypeDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.repository.KpiEventUserRepo;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.GroupTypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupTypeServiceImpl implements GroupTypeService {
    @Autowired
    KpiGroupRepo kpiGroupRepo;
    @Autowired
    KpiEventUserRepo kpiEventUserRepo;

    @Override
    public List<GroupTypeDTO> getAllGroupType() {
        List<GroupTypeDTO> groupTypeDTOS = null;
        List<KpiGroup> kpiGroupTypeEntities = (List<KpiGroup>) kpiGroupRepo.findAll();
        groupTypeDTOS = convertKpiGroupTypeEntityToDTO(kpiGroupTypeEntities);
        return groupTypeDTOS;
    }

    private List<GroupTypeDTO> convertKpiGroupTypeEntityToDTO(List<KpiGroup> kpiGroupTypeEntities) {
        List<GroupTypeDTO> groupTypeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiGroupTypeEntities)) {
            for (KpiGroup kpiGroup : kpiGroupTypeEntities) {
                GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
                BeanUtils.copyProperties(kpiGroup, groupTypeDTO);
                groupTypeDTOS.add(groupTypeDTO);
            }
        }

        return groupTypeDTOS;
    }
}
