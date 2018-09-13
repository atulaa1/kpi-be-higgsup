package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.*;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiEventUserRepo;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.BaseService;
import com.higgsup.kpi.service.EventService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl extends BaseService implements EventService {

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private UserService userService;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Override
    @Transactional
    public EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException {
        EventDTO<List<EventSupportDetail>> eventSupportDTO = new EventDTO<>();
        List<ErrorDTO> validates = validateSupportEvent(supportDTO);
        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent eventSupport = convertEventSupportDTOToEntityForCreate(supportDTO);
            eventSupport = kpiEventRepo.save(eventSupport);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            userService.registerUser(authentication.getPrincipal().toString());
            KpiUser kpiUser = kpiUserRepo.findByUserName(authentication.getPrincipal().toString());

            KpiEventUser kpiEventUser = new KpiEventUser();
            kpiEventUser.setType(EventUserType.MEMBER.getValue());
            KpiEventUserPK kpiEventUserPK = new KpiEventUserPK();
            kpiEventUserPK.setUserName(kpiUser.getUserName());
            kpiEventUserPK.setEventId(eventSupport.getId());
            kpiEventUser.setKpiEventUserPK(kpiEventUserPK);

            kpiEventUserRepo.save(kpiEventUser);

            eventSupportDTO = convertSupportEntiyToDTO(eventSupport);
        } else {
            eventSupportDTO.setErrorCode(validates.get(0).getErrorCode());
            eventSupportDTO.setMessage(validates.get(0).getMessage());
            eventSupportDTO.setErrorDTOS(validates);
        }
        return eventSupportDTO;
    }

    @Override
    @Transactional
    public EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException {
        EventDTO<List<EventSupportDetail>> eventSupportDTO = new EventDTO<>();
        List<ErrorDTO> validates = validateSupportEvent(supportDTO);
        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent eventSupport = convertEventSupportDTOToEntityForUpdate(supportDTO);
            eventSupportDTO = convertSupportEntiyToDTO(eventSupport);

        } else {
            eventSupportDTO.setErrorCode(validates.get(0).getErrorCode());
            eventSupportDTO.setMessage(validates.get(0).getMessage());
            eventSupportDTO.setErrorDTOS(validates);
        }
        return eventSupportDTO;
    }

    @Override
    public EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException {
        Optional<KpiEvent> event = kpiEventRepo.findById(eventDTO.getId());
        if (event.isPresent()) {
            KpiEvent kpiEvent = event.get();
            Calendar calendar = new GregorianCalendar();
            if ((kpiEvent.getCreatedDate().getYear() + 1900 == calendar.get(Calendar.YEAR) && kpiEvent.getCreatedDate()
                    .getMonth() + 1 <= (calendar.get(Calendar.MONTH) + 1) && calendar.get(Calendar.HOUR) <= 10 && calendar.get(
                    Calendar.DATE) <= 16)
                    ||
                    (kpiEvent.getCreatedDate().getYear() + 1900 < calendar.get(Calendar.YEAR) && kpiEvent.getCreatedDate()
                            .getMonth() + 1 > (calendar.get(Calendar.MONTH) + 1) && calendar.get(Calendar.HOUR) <= 10 && calendar
                            .get(Calendar.DATE) <= 16)
                    ) {
                if (Objects.equals(kpiEvent.getStatus(), StatusEvent.WAITING.getValue())) {
                    GroupType groupType = GroupType.getGroupType(kpiEvent.getGroup().getGroupType().getId());
                    switch (groupType) {
                        case SEMINAR:
                            break;
                        case CLUB:
                            break;
                        case TEAM_BUILDING:
                            break;
                        case SUPPORT:
                            eventDTO = confirmOrCancelEventSupport(kpiEvent, eventDTO);
                            break;
                    }
                } else {
                    eventDTO.setErrorCode(ErrorCode.DATA_CAN_NOT_CHANGE.getValue());
                    eventDTO.setMessage(ErrorMessage.EVENT_CONFIRMED_OR_CANCELED);
                }
            }
        } else {
            eventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            eventDTO.setMessage(ErrorMessage.NOT_FIND_EVENT_BY_ID);
        }
        return eventDTO;
    }

    private EventDTO confirmOrCancelEventSupport(KpiEvent kpiEvent, EventDTO eventDTO) throws IOException, NoSuchFieldException,
            IllegalAccessException {
        if (Objects.equals(eventDTO.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
        } else {
            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
        }
        Float point = setHistorySupportAndGetAllPoint(kpiEvent);
        //ad point
        addPoint(kpiEvent.getKpiEventUserList().get(0).getKpiUser(), point);

        kpiEvent = kpiEventRepo.save(kpiEvent);

        eventDTO = convertSupportEntiyToDTO(kpiEvent);
        return eventDTO;
    }

    private Float setHistorySupportAndGetAllPoint(KpiEvent kpiEvent) throws NoSuchFieldException, IllegalAccessException,
            IOException {
        ObjectMapper mapper = new ObjectMapper();
        Float point = 0F;
        TypeReference<List<EventSupportDetail>> tRefBedType = new TypeReference<List<EventSupportDetail>>() {
        };
        List<EventSupportDetail> configEventSupport = mapper.readValue(kpiEvent.getAdditionalConfig(), tRefBedType);
        GroupSupportDetail groupSupportDetail = mapper.readValue(kpiEvent.getGroup().getAdditionalConfig(),
                GroupSupportDetail.class);
        for (EventSupportDetail eventSupportDetail : configEventSupport) {
            Field field = groupSupportDetail.getClass().getDeclaredField(eventSupportDetail.getName());
            field.setAccessible(true);
            Float pointConfig = (Float) field.get(groupSupportDetail);
            eventSupportDetail.setPoint(pointConfig);
            point += pointConfig * eventSupportDetail.getQuantity();
        }
        kpiEvent.setAdditionalConfig(mapper.writeValueAsString(configEventSupport));
        return point;
    }

    private KpiEvent convertEventSupportDTOToEntityForUpdate(EventDTO<List<EventSupportDetail>> supportDTO) throws
            JsonProcessingException {
        KpiEvent eventSupport;
        ObjectMapper mapper = new ObjectMapper();
        eventSupport = kpiEventRepo.findById(supportDTO.getId()).get();

        eventSupport.setBeginDate(supportDTO.getBeginDate());
        String additionalConfigSupport = mapper.writeValueAsString(supportDTO.getAdditionalConfig());
        eventSupport.setAdditionalConfig(additionalConfigSupport);

        eventSupport.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return kpiEventRepo.save(eventSupport);
    }

    private EventDTO<List<EventSupportDetail>> convertSupportEntiyToDTO(KpiEvent kpiEvent) throws IOException {
        EventDTO<List<EventSupportDetail>> eventSupportDTO = new EventDTO<>();
        List<EventSupportDetail> detailsConfigSupport;
        ObjectMapper mapper = new ObjectMapper();
        List<EventUserDTO> eventUserDTOS = new ArrayList<>();

        BeanUtils.copyProperties(kpiEvent, eventSupportDTO);

        TypeReference<List<EventSupportDetail>> tRefBedType = new TypeReference<List<EventSupportDetail>>() {
        };
        detailsConfigSupport = mapper.readValue(kpiEvent.getAdditionalConfig(), tRefBedType);
        eventSupportDTO.setAdditionalConfig(detailsConfigSupport);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        if (!CollectionUtils.isEmpty(kpiEventUsers)) {
            List<String> namesUser = kpiEventUsers.stream().map(kpiEventUser -> kpiEventUser.getKpiEventUserPK().getUserName())
                    .collect(Collectors.toList());
            List<KpiUser> usersEvent = (List<KpiUser>) kpiUserRepo.findAllById(namesUser);

            for (KpiEventUser kpiEventUser : kpiEventUsers) {
                EventUserDTO eventUserDTO = new EventUserDTO();
                eventUserDTO.setType(kpiEventUser.getType());
                Optional<KpiUser> kpiUser = usersEvent.stream().filter(
                        kpiUserTemp -> kpiUserTemp.getUserName().equals(kpiEventUser.getKpiEventUserPK().getUserName()))
                        .findFirst();
                if (kpiUser.isPresent()) {
                    UserDTO userDTO = new UserDTO();
                    KpiUser userDB = kpiUser.get();

                    BeanUtils.copyProperties(userDB, userDTO);
                    userDTO.setUsername(userDB.getUserName());
                    eventUserDTO.setUser(userDTO);

                    eventUserDTOS.add(eventUserDTO);
                }
            }
        }
        eventSupportDTO.setEventUserList(eventUserDTOS);
        eventSupportDTO.setGroup(convertConfigSupportToDTO(kpiEvent.getGroup()));

        return eventSupportDTO;
    }

    private GroupTypeDTO convertGroupTypeEntityToDTO(KpiGroupType kpiGroupType) {
        GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
        BeanUtils.copyProperties(kpiGroupType, groupTypeDTO);
        return groupTypeDTO;
    }

    public GroupDTO convertConfigSupportToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSupportDetail> groupSupportDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, groupSupportDTO);
        GroupSupportDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                GroupSupportDetail.class);

        groupSupportDTO.setAdditionalConfig(groupSeminarDetail);
        groupSupportDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return groupSupportDTO;
    }

    private KpiEvent convertEventSupportDTOToEntityForCreate(EventDTO<List<EventSupportDetail>> supportDTO) throws
            JsonProcessingException {
        KpiEvent eventSupport = new KpiEvent();
        ObjectMapper mapper = new ObjectMapper();

        eventSupport.setStatus(StatusEvent.WAITING.getValue());
        eventSupport.setBeginDate(supportDTO.getBeginDate());
        Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(supportDTO.getGroup().getId());
        if (kpiGroup.isPresent()) {
            KpiGroup kpiGroupDB = kpiGroup.get();
            kpiGroup.ifPresent(eventSupport::setGroup);
            eventSupport.setGroup(kpiGroupDB);

            String additionalConfigSupport = mapper.writeValueAsString(supportDTO.getAdditionalConfig());
            eventSupport.setAdditionalConfig(additionalConfigSupport);
            eventSupport.setName(kpiGroupDB.getName());
        }

        return eventSupport;
    }

    private List<ErrorDTO> validateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) {
        List<ErrorDTO> errors = new ArrayList<>();
        if (Objects.isNull(supportDTO.getBeginDate())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.START_DATE_CAN_NOT_NULL);

            errors.add(errorDTO);
        }

        if (Objects.isNull(supportDTO.getGroup())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.GROUP_CAN_NOT_NULL);

            errors.add(errorDTO);
        } else {
            if (Objects.isNull(supportDTO.getGroup().getId())) {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errorDTO.setMessage(ErrorMessage.GROUP_ID_CAN_NOT_NULL);

                errors.add(errorDTO);
            } else {
                Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(supportDTO.getGroup().getId());
                if (!kpiGroup.isPresent() || !Objects.equals(kpiGroup.get().getGroupType().getId(), GroupType.SUPPORT.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(ErrorMessage.GROUP_ID_NOT_IS_SUPPORT);
                    errors.add(errorDTO);
                }
            }
        }

        if (CollectionUtils.isEmpty(supportDTO.getAdditionalConfig())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.LIST_SUPPORT_CAN_NOT_EMPTY);

            errors.add(errorDTO);
        } else {
            for (int i = 0; i < supportDTO.getAdditionalConfig().size(); i++) {
                EventSupportDetail supportDetail = supportDTO.getAdditionalConfig().get(i);

                if (Objects.isNull(supportDetail.getName())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(String.format(ErrorMessage.NAME_AT_INDEX_CAN_NOT_NULL, i));

                    errors.add(errorDTO);
                } else {
                    try {
                        GroupSupportDetail.class.getDeclaredField(supportDetail.getName());
                    } catch (NoSuchFieldException e) {
                        ErrorDTO errorDTO = new ErrorDTO();

                        errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                        errorDTO.setMessage(String.format(ErrorMessage.NAME_SUPPORT_AT_INDEX_CAN_NOT_INCORRECT, i));

                        errors.add(errorDTO);
                    }
                }
                if (Objects.isNull(supportDetail.getQuantity())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(String.format(ErrorMessage.QUANTITY_AT_INDEX_CAN_NOT_NULL, i));

                    errors.add(errorDTO);
                } else {
                    if (supportDetail.getQuantity() <= 0) {
                        ErrorDTO errorDTO = new ErrorDTO();

                        errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                        errorDTO.setMessage(String.format(ErrorMessage.QUANTITY_AT_INDEX_CAN_NOT_LESS_ONE, i));

                        errors.add(errorDTO);
                    }
                }

            }
        }
        //for update
        if (Objects.nonNull(supportDTO.getId())) {
            Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(supportDTO.getId());

            if (kpiEventOptional.isPresent()) {
                KpiEvent kpiEvent = kpiEventOptional.get();
                if (!Objects.equals(kpiEvent.getGroup().getGroupType().getId(), GroupType.SUPPORT.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(ErrorMessage.GROUP_WITH_ID_NOT_IS_SUPPORT);

                    errors.add(errorDTO);
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(supportDTO.getId());
                if (!(kpiEventUsers.size() > 1)
                        && !(kpiEventUsers.get(0).getKpiEventUserPK().getUserName().equals(authentication.getPrincipal()))) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(HttpStatus.FORBIDDEN.value());
                    errorDTO.setMessage(HttpStatus.FORBIDDEN.getReasonPhrase());

                    errors.add(errorDTO);
                }
            } else {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errorDTO.setMessage(ErrorMessage.ID_NOT_INCORRECT);

                errors.add(errorDTO);
            }

        }

        return errors;
    }
}
