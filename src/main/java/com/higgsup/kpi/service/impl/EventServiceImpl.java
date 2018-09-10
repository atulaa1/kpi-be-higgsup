package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.EventClubDetail;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventUserDTO;
import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiEventUserPK;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiEventUserRepo;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.EventService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws JsonProcessingException {
        EventDTO<EventClubDetail> validatedEventDTO = new EventDTO<>();

        if (kpiEventRepo.findByName(eventDTO.getName()) == null) {
            KpiEvent kpiEvent = new KpiEvent();

            ObjectMapper mapper = new ObjectMapper();
            if (validateClub(eventDTO, validatedEventDTO)) {
                String clubJson = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
                BeanUtils.copyProperties(eventDTO, kpiEvent);

                kpiEvent.setAdditionalConfig(clubJson);
                kpiEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroup> groupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());

                if (groupOptional.isPresent()) {
                    kpiEvent.setGroup(groupOptional.get());
                    kpiEvent = kpiEventRepo.save(kpiEvent);

                    List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());
                    kpiEventUserRepo.saveAll(eventUsers);

                    BeanUtils.copyProperties(kpiEvent, validatedEventDTO);
                    validatedEventDTO.setGroup(eventDTO.getGroup());
                    validatedEventDTO.setAdditionalConfig(eventDTO.getAdditionalConfig());
                    validatedEventDTO.setEventUserList(eventDTO.getEventUserList());
                } else {
                    validatedEventDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
                    validatedEventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                }
            }

        } else {
            validatedEventDTO.setMessage(ErrorMessage.EVENT_ALREADY_EXISTS);
            validatedEventDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
        }
        return validatedEventDTO;
    }

    private List<KpiEventUser> convertEventUsersToEntity(KpiEvent kpiEvent, List<EventUserDTO> eventUserList) {
        List<KpiEventUser> kpiEventUsers = new ArrayList<>();

        for (EventUserDTO eventUserDTO : eventUserList) {
            KpiEventUser kpiEventUser = new KpiEventUser();
            KpiEventUserPK kpiEventUserPK = new KpiEventUserPK();
            userService.registerUser(eventUserDTO.getUser().getUsername());

            kpiEventUserPK.setEventId(kpiEvent.getId());
            kpiEventUserPK.setUserName(eventUserDTO.getUser().getUsername());
            kpiEventUser.setKpiEventUserPK(kpiEventUserPK);

            kpiEventUser.setType(eventUserDTO.getType());
            kpiEventUsers.add(kpiEventUser);

        }
        return kpiEventUsers;
    }

    private Boolean validateClub(EventDTO<EventClubDetail> eventDTO, EventDTO validatedEventDTO) {
        Boolean validate = false;

        if (eventDTO.getName() == null) {
            validatedEventDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getAdditionalConfig().getPointInfo() == null) {
            validatedEventDTO.setMessage(ErrorMessage.POINT_INFO_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getEventUserList().size() == 0) {
            validatedEventDTO.setMessage(ErrorMessage.LIST_OF_PARTICIPANTS_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getBeginDate() == null) {
            validatedEventDTO.setMessage(ErrorMessage.BEGIN_DATE_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getEndDate() == null) {
            validatedEventDTO.setMessage(ErrorMessage.END_DATE_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getBeginDate().after(eventDTO.getEndDate())) {
            validatedEventDTO.setMessage(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getDescription());
            validatedEventDTO.setErrorCode(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getValue());
        } else if (!kpiGroupRepo.findById(eventDTO.getGroup().getId()).isPresent()) {
            validatedEventDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
        } else {

           for(EventUserDTO eventUserDTO : eventDTO.getEventUserList() ) {
                if (eventUserDTO.getType() != 1 && eventUserDTO.getType() != 2 && eventUserDTO.getType() != 3) {
                    validatedEventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    validatedEventDTO.setMessage(ErrorMessage.MEMBER_TYPE_DOES_NOT_EXIST);
                }else {
                    validate = true;
                }
            }
        }
        return validate;
    }
}
