package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.service.GroupService;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    KpiGroupRepo kpiGroupRepo;

    @Autowired
    KpiGroupTypeRepo kpiGroupTypeRepo;

    @Override
    public void create(GroupDTO<TeamBuildingDTO> groupDTO)  throws JsonProcessingException {
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null){
            throw new ServiceException(String.format("Group with name = %s exist!", groupDTO.getName()));
        }
        KpiGroup kpiGroup = new KpiGroup();
        ObjectMapper mapper = new ObjectMapper();
        String jsonConfigTeamBuilding = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
        BeanUtils.copyProperties(groupDTO, kpiGroup);
        kpiGroup.setAdditionalConfig(jsonConfigTeamBuilding);
        kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
        if(kpiGroupType.isPresent()){
            kpiGroup.setGroupTypeId(kpiGroupType.get());

            kpiGroupRepo.save(kpiGroup);
        }else {
            // show eror
        }
    }
}
