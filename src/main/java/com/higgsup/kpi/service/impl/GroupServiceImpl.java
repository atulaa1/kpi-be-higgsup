package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.service.GroupService;
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
    public GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findByName("Support") == null) {
            groupDTO.setMessage(ErrorCode.NOT_FIND.getDescription());
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
        }else {
            if (!validateInfo(groupDTO)) {
                KpiGroup kpiGroup = kpiGroupRepo.findByName("Support");
                groupDTO.setId(kpiGroup.getId());
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
                    groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                }
            } else {
                groupDTO.setErrorCode(ErrorCode.NOT_FILLING_ALL_INFORMATION.getValue());
                groupDTO.setMessage(ErrorMessage.NOT_FILLING_ALL_INFORMATION);
            }
        }
        return groupDTO;
    }

    private boolean validateInfo(GroupDTO<GroupSupportDetail> groupDTO) {
        GroupSupportDetail groupSupportDetail = groupDTO.getAdditionalConfig();
        return (groupSupportDetail.getSupportConferencePoint() == null || groupSupportDetail.getCleanUpPoint() == null ||
                groupSupportDetail.getBuyingStuffPoint() == null || groupSupportDetail.getWeeklyCleanUpPoint() == null ||
                groupSupportDetail.getTrainingPoint() == null);
    }
}
