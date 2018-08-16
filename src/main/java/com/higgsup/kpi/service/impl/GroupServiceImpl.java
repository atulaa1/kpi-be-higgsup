package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
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
            groupDTO.setMessage(ErrorMessage.NOT_FIND_ITEM);
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
                    groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                }
            }
        }
        return groupDTO;
    }

    public GroupDTO validateData(GroupDTO<TeamBuildingDTO> groupDTO){

        GroupDTO validate = new GroupDTO();

        if (kpiGroupRepo.findByName(groupDTO.getName()) != null){
            validate.setErrorCode(ErrorCode.DUPLICATED_ITEM.getValue());
            validate.setMessage(ErrorCode.DUPLICATED_ITEM.getContent());
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getFirstPrize())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.INVALIDATED_FIRST_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getSecondPrize())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.INVALIDATED_SECOND_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getThirdPrize())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.INVALIDATED_THIRD_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getOrganizers())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.INVALIDATED_ORGNIZERS_PRIZE);
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getFirstPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE);
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getThirdPrize())){
            validate.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validate.setMessage(ErrorMessage.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE);
        }

        return validate;
    }
}
