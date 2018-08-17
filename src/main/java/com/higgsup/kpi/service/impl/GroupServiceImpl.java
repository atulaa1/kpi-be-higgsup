package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSeminarDetail;
import com.higgsup.kpi.dto.GroupClubDetail;
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
    public GroupDTO createSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null) {
            groupDTO.setErrorCode(ErrorCode.DUPLICATED_ITEM.getValue());
            groupDTO.setMessage(ErrorCode.DUPLICATED_ITEM.getDescription());
        } else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getHost())) {
            groupDTO.setMessage(ErrorMessage.POINT_HOST_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getMember())) {
            groupDTO.setMessage(ErrorMessage.POINT_MEMBER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getListener())) {
            groupDTO.setMessage(ErrorMessage.POINT_LISTENER_IS_NOT_VALIDATE);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (Double.parseDouble(groupDTO.getAdditionalConfig().getHost()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getMember())) {
            groupDTO.setMessage(ErrorMessage.POINT_HOST_NOT_LARGER_THAN_POINT_MEMBER);
            groupDTO.setErrorCode(ErrorCode.NO_LARGER_THAN.getValue());
        } else if (Double.parseDouble(groupDTO.getAdditionalConfig().getMember()) <= Double.parseDouble(groupDTO.getAdditionalConfig().getListener())) {
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

            if(groupDTO.getName().length()== 0 || groupDTO.getAdditionalConfig().getHost().length() == 0 ){
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.NOT_NULL.getValue());
            } else if (minNumberOfSessions != (int) minNumberOfSessions || String.valueOf(minNumberOfSessions).length() > 2 || String.valueOf(minNumberOfSessions).length()==0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (participationPoint != (float) participationPoint || String.valueOf(participationPoint).substring(1).length() != 2 || String.valueOf(participationPoint).length() == 0) {
                groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            } else if (effectivePoint != (float) effectivePoint || String.valueOf(effectivePoint).substring(1).length() != 2 || String.valueOf(effectivePoint).length() == 0) {
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
}
