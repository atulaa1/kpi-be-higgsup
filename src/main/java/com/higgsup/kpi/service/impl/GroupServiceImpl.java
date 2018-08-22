package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.service.GroupService;
import com.higgsup.kpi.util.UtilsValidate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
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
        if(checkGroupTypeIdExisted(groupDTO,validateGroupDTO)){
            if (!kpiGroupOptional.isPresent()){
                validateGroupDTO.setErrorCode(ErrorCode.NOT_FIND_ITEM.getValue());
                validateGroupDTO.setMessage(ErrorMessage.NOT_FIND_ITEM);
            } else if(validateTeambuildingInfo(groupDTO, validateGroupDTO)) {
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
        }
        return validateGroupDTO;
    }

    @Override
    public GroupDTO createSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null) {
            groupDTO.setErrorCode(ErrorCode.DUPLICATED_ITEM.getValue());
            groupDTO.setMessage(ErrorCode.DUPLICATED_ITEM.getDescription());
        } else if (!UtilsValidate.pointValidate(String.valueOf(groupDTO.getAdditionalConfig().getHost()))){
            groupDTO.setMessage(ErrorMessage.POINT_HOST_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate((String.valueOf(groupDTO.getAdditionalConfig().getMember())))) {
            groupDTO.setMessage(ErrorMessage.POINT_MEMBER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate((String.valueOf(groupDTO.getAdditionalConfig().getListener())))) {
            groupDTO.setMessage(ErrorMessage.POINT_LISTENER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (Double.parseDouble((String.valueOf(groupDTO.getAdditionalConfig().getHost()))) <= Double.parseDouble((String.valueOf(groupDTO.getAdditionalConfig().getMember())))) {
            groupDTO.setMessage(ErrorMessage.POINT_HOST_NOT_LARGER_THAN_POINT_MEMBER);
            groupDTO.setErrorCode(ErrorCode.NO_LARGER_THAN.getValue());
        } else if (Double.parseDouble((String.valueOf(groupDTO.getAdditionalConfig().getMember()))) <= Double.parseDouble((String.valueOf(groupDTO.getAdditionalConfig().getListener())))) {
            groupDTO.setMessage(ErrorMessage.POINT_MEMBER_NOT_LARGER_THAN_POINT_LISTENER);
            groupDTO.setErrorCode(ErrorCode.NO_LARGER_THAN.getValue());
        }else {
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();
            String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
            BeanUtils.copyProperties(groupDTO, kpiGroup);
            kpiGroup.setAdditionalConfig(jsonConfigSeminar);
            kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
            if (kpiGroupType.isPresent()) {
                kpiGroup.setGroupTypeId(kpiGroupType.get());
                kpiGroupRepo.save(kpiGroup);
            } else {
                groupDTO.setMessage(ErrorMessage.NOT_FIND_SEMINAR);
                groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        }
        return groupDTO;
    }
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
    public GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findById(groupDTO.getId())==null){
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            groupDTO.setMessage(ErrorMessage.NOT_FIND_SEMINAR);
        }else if (!UtilsValidate.pointValidate(String.valueOf(groupDTO.getAdditionalConfig().getHost()))) {
            groupDTO.setMessage(ErrorMessage.POINT_HOST_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate(String.valueOf(groupDTO.getAdditionalConfig().getMember()))){
            groupDTO.setMessage(ErrorMessage.POINT_MEMBER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate(String.valueOf(groupDTO.getAdditionalConfig().getListener()))) {
            groupDTO.setMessage(ErrorMessage.POINT_LISTENER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (Double.parseDouble(String.valueOf(groupDTO.getAdditionalConfig().getHost()) )<= Double.parseDouble(String.valueOf(groupDTO.getAdditionalConfig().getMember()))) {
            groupDTO.setMessage(ErrorMessage.POINT_HOST_NOT_LARGER_THAN_POINT_MEMBER);
            groupDTO.setErrorCode(ErrorCode.NO_LARGER_THAN.getValue());
        } else if (Double.parseDouble(String.valueOf(groupDTO.getAdditionalConfig().getMember())) <= Double.parseDouble(String.valueOf(groupDTO.getAdditionalConfig().getListener()))) {
            groupDTO.setMessage(ErrorMessage.POINT_MEMBER_NOT_LARGER_THAN_POINT_LISTENER);
            groupDTO.setErrorCode(ErrorCode.NO_LARGER_THAN.getValue());
        }else {
            Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(groupDTO.getId());
            if (kpiGroupOptional.isPresent()){
                KpiGroup kpiGroup = kpiGroupOptional.get();
                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup,"id");
                String seminarJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                kpiGroup.setAdditionalConfig(seminarJson);
                kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
                if (kpiGroupType.isPresent())
                {
                    kpiGroup.setGroupTypeId(kpiGroupType.get());
                    kpiGroupRepo.save(kpiGroup);
                } else {
                    groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                    groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                }
            }
        }
        return groupDTO;
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
        KpiGroup findGroupTypeId = kpiGroupRepo.findGroupTypeId(groupTypeId);
        if (checkGroupTypeIdExisted(groupDTO,validateGroupDTO)){
            if(findGroupTypeId == null){
                if (validateTeambuildingInfo(groupDTO, validateGroupDTO)) {
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
        }

        return validateGroupDTO;
    }

    @Override
    public GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
        if(kpiGroupType.isPresent()) {
            if (kpiGroupRepo.findByGroupTypeId(kpiGroupType) != null) {
                groupDTO.setErrorCode(ErrorCode.ALREADY_CREATED.getValue());
                groupDTO.setMessage(ErrorMessage.ALREADY_CREATED);
            } else {
                if (validateNullInformation(groupDTO)) {
                    if (!validatePoint(groupDTO)) {
                        groupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                        groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    } else {
                        KpiGroup kpiGroup = new KpiGroup();
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
        }
        else{
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
        }
        return groupDTO;
    }


    @Override
    public GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());
        if(kpiGroupType.isPresent()) {
            if (kpiGroupRepo.findByGroupTypeId(kpiGroupType) == null) {
                groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
            } else {
                if (validateNullInformation(groupDTO)) {
                    if (!validatePoint(groupDTO)) {
                        groupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                        groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    } else {
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
        }else{
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
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

    Boolean validateTeambuildingInfo(GroupDTO<TeamBuildingDTO> groupDTO, GroupDTO validateGroupDTO){
        boolean validate = false;

        if(groupDTO.getAdditionalConfig().getFirstPrize().length() == 0){
            validateGroupDTO.setErrorCode(ErrorCode.TEAMBUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAMBUILDING_FIRST_PRIZE_SCORE_CAN_NOT_NULL);
        }
        else if(groupDTO.getAdditionalConfig().getSecondPrize().length() == 0){
            validateGroupDTO.setErrorCode(ErrorCode.TEAMBUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAMBUILDING_SECOND_PRIZE_SCORE_CAN_NOT_NULL);
        }
        else if(groupDTO.getAdditionalConfig().getThirdPrize().length() == 0){
            validateGroupDTO.setErrorCode(ErrorCode.TEAMBUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAMBUILDING_THIRD_PRIZE_SCORE_CAN_NOT_NULL);
        }
        else if(groupDTO.getAdditionalConfig().getOrganizers().length() == 0){
            validateGroupDTO.setErrorCode(ErrorCode.TEAMBUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAMBUILDING_ORGNIZERS_SCORE_CAN_NOT_NULL);
        }
        else if(!UtilsValidate.isNumber(groupDTO.getAdditionalConfig().getFirstPrize())){
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
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getFirstPrize()) <=
                Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE);
        }
        else if(Double.parseDouble(groupDTO.getAdditionalConfig().getSecondPrize()) <=
                  Double.parseDouble(groupDTO.getAdditionalConfig().getThirdPrize())){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE);
        }
        else {
            validate = true;
        }
        return validate;
    }

    Boolean checkGroupTypeIdExisted(GroupDTO<TeamBuildingDTO> groupDTO, GroupDTO validateGroupDTO){
        boolean isExitested = false;
        if (groupDTO.getGroupTypeId().getId() == null){
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.GROUP_TYPE_ID_CAN_NOT_NULL);
            isExitested = false;
        } else {
            isExitested = true;
        }
        return isExitested;
    }

    @Override
    public List<GroupDTO> getAllGroup() throws IOException {
        List<KpiGroup> groupList = (List<KpiGroup>) kpiGroupRepo.findAll();
        return convertGroupsEntityToDTO(groupList);
    }

    private List<GroupDTO> convertGroupsEntityToDTO(List<KpiGroup> groupList) throws IOException {
        List<GroupDTO> groupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupList)) {
            for (KpiGroup kpiGroup : groupList) {
                switch (GroupType.getGroupType(kpiGroup.getGroupTypeId().getId())) {
                    case SEMINAR:
                        groupDTOS.add(convertConfigSeminarToDTO(kpiGroup));
                        break;
                    case CLUB:
                        groupDTOS.add(convertConfigClubToDTO(kpiGroup));
                        break;
                    case TEAM_BUILDING:
                        groupDTOS.add(convertConfigTeamBuildingToDTO(kpiGroup));
                        break;
                    case SUPPORT:
                        groupDTOS.add(convertConfigSupportToDTO(kpiGroup));
                        break;
                }
            }
        }
        return groupDTOS;
    }

    private GroupDTO convertConfigSupportToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSupportDetail> groupSupportDTO = new GroupDTO<>();
        BeanUtils.copyProperties(kpiGroup, groupSupportDTO);
        GroupSupportDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), GroupSupportDetail.class);
        groupSupportDTO.setAdditionalConfig(groupSeminarDetail);
        groupSupportDTO.setGroupTypeId(convertGroupTypeEntityToDTO(kpiGroup.getGroupTypeId()));
        return groupSupportDTO;
    }

    private GroupDTO convertConfigTeamBuildingToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<TeamBuildingDTO> teamBuildingGroupDTO = new GroupDTO<>();
        BeanUtils.copyProperties(kpiGroup, teamBuildingGroupDTO);
        TeamBuildingDTO groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), TeamBuildingDTO.class);
        teamBuildingGroupDTO.setAdditionalConfig(groupSeminarDetail);
        teamBuildingGroupDTO.setGroupTypeId(convertGroupTypeEntityToDTO(kpiGroup.getGroupTypeId()));
        return teamBuildingGroupDTO;
    }

    private GroupDTO convertConfigSeminarToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSeminarDetail> seminarGroupDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, seminarGroupDTO);
        GroupSeminarDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), GroupSeminarDetail.class);
        seminarGroupDTO.setAdditionalConfig(groupSeminarDetail);
        seminarGroupDTO.setGroupTypeId(convertGroupTypeEntityToDTO(kpiGroup.getGroupTypeId()));
        return seminarGroupDTO;
    }

    private GroupDTO convertConfigClubToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupClubDetail> groupClubDTO = new GroupDTO<>();
        BeanUtils.copyProperties(kpiGroup, groupClubDTO);
        GroupClubDetail groupClubDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), GroupClubDetail.class);
        groupClubDTO.setAdditionalConfig(groupClubDetail);
        groupClubDTO.setGroupTypeId(convertGroupTypeEntityToDTO(kpiGroup.getGroupTypeId()));
        return groupClubDTO;
    }

    private GroupTypeDTO convertGroupTypeEntityToDTO(KpiGroupType kpiGroupType) {
        GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
        BeanUtils.copyProperties(kpiGroupType, groupTypeDTO);
        return groupTypeDTO;
    }

}
