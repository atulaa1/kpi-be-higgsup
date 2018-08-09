package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.GroupService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    KpiGroupRepo kpiGroupRepo;

    @Override
    public void createSeminar(GroupDTO groupDTO) {
        if (kpiGroupRepo.findByName(groupDTO.getName()) == null) {
            KpiGroup kpiGroup = new KpiGroup();
            BeanUtils.copyProperties( groupDTO,kpiGroup);
            kpiGroupRepo.save(kpiGroup);
        }else {
            throw new ServiceException(String.format("Author with id = %d does not exist!"));
        }
    }


}
