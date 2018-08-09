package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.GroupService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    KpiGroupRepo kpiGroupRepo;

    @Override
    public void create(GroupDTO groupDTO) {
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null){
            throw new ServiceException(String.format("Group with name = %s exist!", groupDTO.getName()));
        }
        KpiGroup kpiGroup = new KpiGroup();
        BeanUtils.copyProperties(groupDTO, kpiGroup);
        kpiGroupRepo.save(kpiGroup);
    }
}
