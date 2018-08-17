package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupTypeDTO;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.repository.KpiEventUserRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
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
    KpiGroupTypeRepo kpiGroupTypeRepo;
    @Autowired
    KpiEventUserRepo kpiEventUserRepo;

    @Override
    public List<GroupTypeDTO> getAllGroupType() {
        List<GroupTypeDTO> groupTypeDTOS = null;
        List<KpiGroupType> kpiGroupTypeEntities = (List<KpiGroupType>) kpiGroupTypeRepo.findAll();
        groupTypeDTOS = convertKpiGroupTypeEntityToDTO(kpiGroupTypeEntities);
        return groupTypeDTOS;
    }

    private List<GroupTypeDTO> convertKpiGroupTypeEntityToDTO(List<KpiGroupType> kpiGroupTypeEntities) {
        List<GroupTypeDTO> groupTypeDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiGroupTypeEntities)) {
            for (KpiGroupType kpiGroupType : kpiGroupTypeEntities) {
                GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
                BeanUtils.copyProperties(kpiGroupType, groupTypeDTO);
                groupTypeDTOS.add(groupTypeDTO);
            }
        }

        return groupTypeDTOS;
    }
}
