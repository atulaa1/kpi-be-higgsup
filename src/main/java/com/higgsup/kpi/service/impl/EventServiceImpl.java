package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.EventClubDetail;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Override
    public EventDTO createClubEvent(EventDTO<EventClubDetail> eventDTO) throws JsonProcessingException {
        EventDTO<EventClubDetail> validatedEventDTO = new EventDTO<>();

        if (kpiEventRepo.findByName(eventDTO.getName()) == null){
            KpiEvent kpiEvent = new KpiEvent();
            ObjectMapper mapper = new ObjectMapper();

            if (validateClubEvent(eventDTO, validatedEventDTO)){
                String clubJson = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
                BeanUtils.copyProperties(eventDTO, kpiEvent);

                kpiEvent.setAdditionalConfig(clubJson);
                kpiEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));
                Optional<KpiGroup> groupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());

                if (groupOptional.isPresent()){
                    kpiEvent.setGroup(groupOptional.get());
                    kpiEvent = kpiEventRepo.save(kpiEvent);

                    BeanUtils.copyProperties(kpiEvent, validatedEventDTO);
                    validatedEventDTO.setGroup(eventDTO.getGroup());
                    validatedEventDTO.setAdditionalConfig(eventDTO.getAdditionalConfig());
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

    private Boolean validateClubEvent(EventDTO<EventClubDetail> eventDTO, EventDTO validatedEventDTO){
        Boolean validate = false;

        if (eventDTO.getName() == null){
            validatedEventDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getAdditionalConfig().getPointInfo() == null){
            validatedEventDTO.setMessage(ErrorMessage.POINT_INFO_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getParticipants().size() == 0){
            validatedEventDTO.setMessage(ErrorMessage.LIST_OF_PARTICIPANTS_CAN_NOT_NULL);
            validatedEventDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (eventDTO.getBeginDate().after(eventDTO.getEndDate())){
            validatedEventDTO.setMessage(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getDescription());
            validatedEventDTO.setErrorCode(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getValue());
        } else{
            validate = true;
        }
        return validate;
    }
}
