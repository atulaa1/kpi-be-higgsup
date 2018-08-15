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
    public GroupDTO updateTeamBuildingActivity(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException {
        Integer id = groupDTO.getId();
        if (kpiGroupRepo.findById(id) == null){
            groupDTO.setErrorCode(ErrorCode.NOT_FIND_ITEM.getValue());
            groupDTO.setMessage(ErrorCode.NOT_FIND_ITEM.getContent());
        }
        else if(validateData(groupDTO) == null) {
            Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(id);
            if (kpiGroupOptional.isPresent()) {
                KpiGroup kpiGroup = kpiGroupOptional.get();
                groupDTO.setId(kpiGroup.getId());
                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup);
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                kpiGroup.setAdditionalConfig(clubJson);
                kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());
                if (kpiGroupType.isPresent()) {
                    kpiGroupOptional.get().setGroupTypeId(kpiGroupType.get());
                    kpiGroupRepo.save(kpiGroupOptional.get());
                } else {
                    groupDTO.setErrorCode(ErrorCode.NOT_FIND_GROUP_TYPE.getValue());
                    groupDTO.setMessage(ErrorCode.NOT_FIND_GROUP_TYPE.getContent());
                }
            }
        }
        return groupDTO;
    }

    public GroupDTO validateData(GroupDTO<TeamBuildingDTO> groupDTO){
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
        } else {
            groupDTO = null;
        }
        return groupDTO;
    }
}
