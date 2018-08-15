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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    KpiGroupRepo kpiGroupRepo;

    @Autowired
    KpiGroupTypeRepo kpiGroupTypeRepo;

    @Override
    public GroupDTO createSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        GroupDTO groupDTO1 = new GroupDTO();
        if (kpiGroupRepo.findByName(groupDTO.getName()) != null)
        {
            groupDTO.setErrorCode(ErrorCode.DUPLICATED_ITEM.getValue());
            groupDTO.setMessage(ErrorCode.DUPLICATED_ITEM.getContent());
        }
        else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getHost()))
        {
            groupDTO1.setMessage(ErrorMessage.POINT_HOST_IS_NOT_VALIDATE);
            groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        }
        else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getMember()))
        {
            groupDTO1.setMessage(ErrorMessage.POINT_MEMBER_IS_NOT_VALIDATE);
            groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        }
        else if (!UtilsValidate.pointValidate(groupDTO.getAdditionalConfig().getMember()))
        {
            groupDTO1.setMessage(ErrorMessage.POINT_LISTENER_IS_NOT_VALIDATE);
            groupDTO1.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        }
        else
        {
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
}

