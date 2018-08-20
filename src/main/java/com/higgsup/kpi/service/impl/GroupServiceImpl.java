package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.service.GroupService;
import com.higgsup.kpi.util.UtilsValidate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

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
        } else if(validateData(groupDTO, validateGroupDTO)) {
                KpiGroup kpiGroup = kpiGroupOptional.get();
                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup,"id");
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                kpiGroup.setAdditionalConfig(clubJson);
                kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
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

    @Override
    public GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException {
        GroupDTO groupDTO1 = new GroupDTO();
        Integer minNumberOfSessions = groupDTO.getAdditionalConfig().getMinNumberOfSessions();
        Float participationPoint = groupDTO.getAdditionalConfig().getParticipationPoint();
        Float effectivePoint = groupDTO.getAdditionalConfig().getEffectivePoint();
        if (kpiGroupRepo.findByName(groupDTO.getName()) == null) {
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();

            if (groupDTO.getName().length() == 0 || groupDTO.getAdditionalConfig().getHost().length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.NOT_NULL.getValue());
            } else if (minNumberOfSessions != (int) minNumberOfSessions || String.valueOf(minNumberOfSessions).length() > 2
                    || String.valueOf(minNumberOfSessions).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (participationPoint != (float) participationPoint || String.valueOf(participationPoint).substring(1).length() != 2
                    || String.valueOf(participationPoint).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (effectivePoint != (float) effectivePoint || String.valueOf(effectivePoint).substring(1).length() != 2
                    || String.valueOf(effectivePoint).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else {
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                BeanUtils.copyProperties(groupDTO, kpiGroup);
                kpiGroup.setAdditionalConfig(clubJson);
                kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());

                if (kpiGroupType.isPresent()) {
                    kpiGroup.setGroupTypeId(kpiGroupType.get());
                    kpiGroupRepo.save(kpiGroup);
                } else {
                    groupDTO1.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                    groupDTO1.setErrorCode(ErrorCode.NOT_FIND.getValue());
                }
            }

        } else {
            groupDTO1.setMessage(ErrorMessage.GROUP_ALREADY_EXISTS);
            groupDTO1.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
        }
        return groupDTO1;
    }

    @Override
    public GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException {
        Integer id = groupDTO.getId();
        GroupDTO groupDTO1 = new GroupDTO();
        Integer minNumberOfSessions = groupDTO.getAdditionalConfig().getMinNumberOfSessions();
        Float participationPoint = groupDTO.getAdditionalConfig().getParticipationPoint();
        Float effectivePoint = groupDTO.getAdditionalConfig().getEffectivePoint();

        if (kpiGroupRepo.findById(id) == null) {
            groupDTO1.setMessage(ErrorCode.NOT_FIND.getDescription());
            groupDTO1.setErrorCode(ErrorCode.NOT_FIND.getValue());
        } else {
            if (groupDTO.getName().length() == 0 || groupDTO.getAdditionalConfig().getHost().length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.NOT_NULL.getValue());
            } else if (minNumberOfSessions != (int) minNumberOfSessions || minNumberOfSessions <= 0 || String.valueOf(minNumberOfSessions).length() > 2 || String.valueOf(minNumberOfSessions).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (participationPoint != (float) participationPoint || participationPoint < 0 || String.valueOf(participationPoint).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (effectivePoint != (float) effectivePoint || effectivePoint < 0|| String.valueOf(effectivePoint).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else {
                Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(id);
                if (kpiGroupOptional.isPresent()) {
                    KpiGroup kpiGroup = kpiGroupOptional.get();
                    groupDTO.setId(kpiGroup.getId());
                    ObjectMapper mapper = new ObjectMapper();
                    BeanUtils.copyProperties(groupDTO, kpiGroup);
                    String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                    kpiGroup.setAdditionalConfig(clubJson);
                    kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());

                    if (kpiGroupType.isPresent()) {
                        kpiGroup.setGroupTypeId(kpiGroupType.get());
                        kpiGroupRepo.save(kpiGroup);
                    } else {
                        groupDTO1.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                        groupDTO1.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    }
                }
            }
        }
        return groupDTO1;
    }

    @Override
    public GroupDTO createConfigTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO)  throws JsonProcessingException {
        GroupDTO validateGroupDTO = new GroupDTO();
        Integer groupTypeId = groupDTO.getGroupTypeId().getId();

        KpiGroup checkGroupTypeId = kpiGroupRepo.findGroupTypeId(groupTypeId);
        if(checkGroupTypeId == null){
            if (validateData(groupDTO, validateGroupDTO)) {
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
                    validateGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    validateGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                }
            }
        } else {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAMBUIDING_HAS_BEEN_EXISTED);
        }
        return validateGroupDTO;
    }

    @Override
    public GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
        if(kpiGroupRepo.findByGroupTypeId(kpiGroupType) != null)
        {
            groupDTO.setErrorCode(ErrorCode.ALREADY_CREATED.getValue());
            groupDTO.setMessage(ErrorMessage.ALREADY_CREATED);
        }
        else {
            if (validateNullInformation(groupDTO)) {
                if(!validatePoint(groupDTO))
                {
                    groupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                    groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                }
                else {
                    KpiGroup kpiGroup = kpiGroupRepo.findByGroupTypeId(kpiGroupType);
                    groupDTO.setId(kpiGroup.getId());
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                    BeanUtils.copyProperties(groupDTO, kpiGroup);
                    kpiGroup.setAdditionalConfig(jsonConfigSeminar);
                    kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    kpiGroup.setGroupTypeId(kpiGroupType.get());
                    kpiGroupRepo.save(kpiGroup);
                }
            } else {
                groupDTO.setErrorCode(ErrorCode.NOT_FILLING_ALL_INFORMATION.getValue());
                groupDTO.setMessage(ErrorMessage.NOT_FILLING_ALL_INFORMATION);
            }
        }
        return groupDTO;
    }

    @Override
    public GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
        if (kpiGroupRepo.findByGroupTypeId(kpiGroupType) == null) {
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
        } else {
            if (validateNullInformation(groupDTO)) {
                if(!validatePoint(groupDTO))
                {
                    groupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                    groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                }
                else {
                    KpiGroup kpiGroup = kpiGroupRepo.findByGroupTypeId(kpiGroupType);
                    groupDTO.setId(kpiGroup.getId());
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                    BeanUtils.copyProperties(groupDTO, kpiGroup);
                    kpiGroup.setAdditionalConfig(jsonConfigSeminar);
                    kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                    kpiGroup.setGroupTypeId(kpiGroupType.get());
                    kpiGroupRepo.save(kpiGroup);
                }
            } else {
                groupDTO.setErrorCode(ErrorCode.NOT_FILLING_ALL_INFORMATION.getValue());
                groupDTO.setMessage(ErrorMessage.NOT_FILLING_ALL_INFORMATION);
            }
        }
        return groupDTO;
    }

    private boolean validateNullInformation(GroupDTO<GroupSupportDetail> groupDTO) {
        GroupSupportDetail groupSupportDetail = groupDTO.getAdditionalConfig();
        return (groupSupportDetail.getSupportConferencePoint() != null && groupSupportDetail.getCleanUpPoint() != null &&
                groupSupportDetail.getBuyingStuffPoint() != null && groupSupportDetail.getWeeklyCleanUpPoint() != null &&
                groupSupportDetail.getTrainingPoint() != null);
    }

    private boolean validatePoint(GroupDTO<GroupSupportDetail> groupDTO)
    {
        return (UtilsValidate.isValidPoint(String.valueOf(groupDTO.getAdditionalConfig().getWeeklyCleanUpPoint()))
                && UtilsValidate.isValidPoint(String.valueOf(groupDTO.getAdditionalConfig().getBuyingStuffPoint()))
                && UtilsValidate.isValidPoint(String.valueOf(groupDTO.getAdditionalConfig().getCleanUpPoint()))
                && UtilsValidate.isValidPoint(String.valueOf(groupDTO.getAdditionalConfig().getSupportConferencePoint()))
                && UtilsValidate.isValidPoint(String.valueOf(groupDTO.getAdditionalConfig().getTrainingPoint())));
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
