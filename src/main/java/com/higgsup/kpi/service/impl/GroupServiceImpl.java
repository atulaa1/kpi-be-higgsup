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
    private KpiGroupRepo kpiGroupRepo;

    @Override
    public void createClub(GroupDTO groupDTO) {
        String name = groupDTO.getName();
        if (kpiGroupRepo.findByName(name)== null){
            KpiGroup kpiGroup = new KpiGroup();
            BeanUtils.copyProperties(groupDTO, kpiGroup);
            kpiGroupRepo.save(kpiGroup);
        }else{
            throw new ServiceException(String.format("Club with name = %s already exists!", name));
        }
    }

}
