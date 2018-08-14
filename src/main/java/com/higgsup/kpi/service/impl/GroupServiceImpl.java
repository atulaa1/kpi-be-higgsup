package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    KpiGroupTypeRepo kpiGroupTypeRepo;

    @Override
    public void createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException {
        if (kpiGroupRepo.findByName(groupDTO.getName()) == null) {
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();

            if (!nameValidation(groupDTO.getAdditionalConfig().getName()) || !nameValidation(groupDTO.getAdditionalConfig().getHost())) {
                groupDTO.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
                groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            }

            if (!minNumberOfSessionsValidation(groupDTO.getAdditionalConfig().getMinNumberOfSessions())) {
                groupDTO.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
                groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            }

            if (!pointValidation(groupDTO.getAdditionalConfig().getEffectivePoint()) && !pointValidation(groupDTO.getAdditionalConfig().getParticipationPoint())) {
                groupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                groupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            }
            String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
            BeanUtils.copyProperties(groupDTO, kpiGroup);
            kpiGroup.setAdditionalConfig(clubJson);
            kpiGroup.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupTypeId().getId());

            if (kpiGroupType.isPresent()) {
                kpiGroup.setGroupTypeId(kpiGroupType.get());
                kpiGroupRepo.save(kpiGroup);
            } else {
                groupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                groupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        } else {
            groupDTO.setMessage(ErrorMessage.GROUP_ALREADY_EXISTS);
            groupDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
        }
    }

    @Override
    public Boolean nameValidation(String name) {
        String patternvalidation = "[A-Z][a-z]+( [A-Z][a-z]+)";
        Pattern pattern = Pattern.compile(patternvalidation);
        Matcher matcher = pattern.matcher(name);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean minNumberOfSessionsValidation(Integer minNumberOfSessions) {
        if (minNumberOfSessions == (int) minNumberOfSessions && String.valueOf(minNumberOfSessions).length() < 3) {
            return true;
        }
        return false;
    }

    @Override
    public Boolean pointValidation(Float point) {
        if (point == (float) point && String.valueOf(point).substring(1).length() == 2) {
            return true;
        }
        return false;
    }

}

