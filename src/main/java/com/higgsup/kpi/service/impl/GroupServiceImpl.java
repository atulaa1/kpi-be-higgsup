package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
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
    public Response createClub(GroupDTO groupDTO) {
        Response response = new Response(200);
        String name = groupDTO.getName();
        if (kpiGroupRepo.findByName(name)== null){
            KpiGroup kpiGroup = new KpiGroup();
            BeanUtils.copyProperties(groupDTO, kpiGroup);
            kpiGroupRepo.save(kpiGroup);
            response.setMessage("A new club activity is created!");
        }else{
            response.setMessage("Club with name = %s already exists!");
        }
        return response;
    }

}
