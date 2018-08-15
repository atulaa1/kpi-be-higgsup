package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.service.GroupService;
import com.higgsup.kpi.util.UtilsValidate;
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
    public GroupDTO create(GroupDTO<TeamBuildingDTO> groupDTO)  throws JsonProcessingException {
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null){
            groupDTO.setErrorCode(ErrorCode.DUPLICATED_ITEM.getValue());
            groupDTO.setMessage(ErrorCode.DUPLICATED_ITEM.getContent());
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getFirstPrize())){
            groupDTO.setErrorCode(ErrorCode.INVALIDATED_FIRST_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.INVALIDATED_FIRST_PRIZE.getContent());
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getSecondPrize())){
            groupDTO.setErrorCode(ErrorCode.INVALIDATED_SECOND_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.INVALIDATED_SECOND_PRIZE.getContent());
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getThirdPrize())){
            groupDTO.setErrorCode(ErrorCode.INVALIDATED_THIRD_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.INVALIDATED_THIRD_PRIZE.getContent());
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getOrganizers())){
            groupDTO.setErrorCode(ErrorCode.INVALIDATED_ORGNIZERS_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.INVALIDATED_ORGNIZERS_PRIZE.getContent());
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getFirstPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize())){
            groupDTO.setErrorCode(ErrorCode.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE.getContent());
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getThirdPrize())){
            groupDTO.setErrorCode(ErrorCode.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE.getValue());
            groupDTO.setMessage(ErrorCode.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE.getContent());
        }
        else{
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();
            String jsonConfigTeamBuilding = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
            BeanUtils.copyProperties(groupDTO, kpiGroup);
            kpiGroup.setAdditionalConfig(jsonConfigTeamBuilding);
            kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());
            if(kpiGroupType.isPresent()){
                kpiGroup.setGroupTypeId(kpiGroupType.get());
                kpiGroupRepo.save(kpiGroup);
            }else {
                groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                groupDTO.setMessage(ErrorCode.NOT_FIND.getContent());
            }
        }
        return groupDTO;
    }
}
