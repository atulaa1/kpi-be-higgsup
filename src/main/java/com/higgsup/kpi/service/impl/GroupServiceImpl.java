package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.GroupDTO;
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

    @Autowired
    KpiGroupTypeRepo kpiGroupTypeRepo;

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
            } else if (minNumberOfSessions != (int) minNumberOfSessions || String.valueOf(minNumberOfSessions).length() > 2 || String.valueOf(minNumberOfSessions).length() == 0) {
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
                } else if (minNumberOfSessions != (int) minNumberOfSessions || String.valueOf(minNumberOfSessions).length() > 2 || String.valueOf(minNumberOfSessions).length() == 0) {
                    groupDTO1.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
                    groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                } else if (participationPoint != (float) participationPoint || String.valueOf(participationPoint).substring(1).length() != 2 || String.valueOf(participationPoint).length() == 0) {
                    groupDTO1.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                    groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                } else if (effectivePoint != (float) effectivePoint || String.valueOf(effectivePoint).substring(1).length() != 2 || String.valueOf(effectivePoint).length() == 0) {
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

    }