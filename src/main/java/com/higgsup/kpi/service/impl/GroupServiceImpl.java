package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSeminarDetail;
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
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiGroupTypeRepo kpiGroupTypeRepo;

    @Override
    public GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findById(groupDTO.getId())==null){
            groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            groupDTO.setMessage(ErrorMessage.NOT_FIND_SEMINAR);
        }else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getHost())) {
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
            Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(groupDTO.getId());
            if (kpiGroupOptional.isPresent()){
                KpiGroup kpiGroup = kpiGroupOptional.get();
                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup,"id");
                String seminarJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                kpiGroup.setAdditionalConfig(seminarJson);
                kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());

                if (kpiGroupType.isPresent()) {
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
}
