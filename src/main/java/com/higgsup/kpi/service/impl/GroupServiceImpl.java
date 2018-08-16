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
        GroupDTO validateGroupDTO = new GroupDTO();
        Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(id);
        if (!kpiGroupOptional.isPresent()){
            validateGroupDTO.setErrorCode(ErrorCode.NOT_FIND_ITEM.getValue());
            validateGroupDTO.setMessage(ErrorMessage.NOT_FIND_ITEM);
        }
        else if(validateData(groupDTO, validateGroupDTO)) {
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
                    validateGroupDTO.setErrorCode(ErrorCode.NOT_FIND_GROUP_TYPE.getValue());
                    validateGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                }
        }
        return validateGroupDTO;
    }

    Boolean validateData(GroupDTO<TeamBuildingDTO> groupDTO, GroupDTO validateGroupDTO){
        boolean validate = false;
        if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getFirstPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_FIRST_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getSecondPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_SECOND_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getThirdPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_THIRD_PRIZE);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getOrganizers())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_ORGNIZERS_PRIZE);
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getFirstPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE);
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getThirdPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE);
        } else {
            validate = true;
        }
        return validate;
    }
}
