package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.*;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import java.util.function.Function;
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

    @Autowired
    private LdapUserService ldapUserService;

    @Autowired
    private Environment environment;

    @Autowired
    private KpiSupportRepo kpiSupportRepo;

    @Autowired
    private KpiSeminarSurveyRepo kpiseminarSurveyRepo;

    @Autowired
    private PointService pointService;

    @Override
    public List<EventDTO> getAllClubAndSupportEvent() throws IOException {
        List<KpiEvent> eventList = kpiEventRepo.findClubAndSupportEvent();
        return convertEventEntityToDTO(eventList);
    }

    public List<EventDTO> getTeamBuildingEvents() throws IOException {
        List<KpiEvent> teamBuildingEvents = kpiEventRepo.findTeamBuildingEvent();
        return convertEventEntityToDTO(teamBuildingEvents);
    }

    @Override
    public List<EventDTO> getAllClubAndSupportEventNewSupport() throws IOException {
        List<KpiEvent> eventList = kpiEventRepo.findClubAndSupportEvent();
        return convertEventEntityToDTONewSupport(eventList);
    }

    @Override
    public List<EventDTO> getEventCreatedByUser(String username) throws IOException {
        List<KpiEvent> eventList = kpiEventRepo.findEventCreatedByUser(username);
        return convertEventEntityToDTO(eventList);
    }

    public List<EventDTO> getSeminarEventByUser(String username) throws IOException {
        List<KpiEvent> seminarEvents = kpiEventRepo.findSeminarEventByUser(username);
        return convertEventEntityToDTO(seminarEvents);
    }

    private List<EventDTO> convertEventEntityToDTO(List<KpiEvent> kpiEventEntities) throws IOException {
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiEventEntities)) {
            for (KpiEvent kpiEvent : kpiEventEntities) {
                switch (GroupType.getGroupType(kpiEvent.getGroup().getGroupType().getId())) {
                    case CLUB:
                        eventDTOS.add(convertEventClubEntityToDTO(kpiEvent));
                        break;
                    case SUPPORT:
                        eventDTOS.add(convertSupportEntiyToDTO(kpiEvent));
                        break;
                    case SEMINAR:
                        eventDTOS.add(convertSeminarEntityToDTO(kpiEvent));
                        break;
                    case TEAM_BUILDING:
                        eventDTOS.add(convertEventTeamBuildingEntityToDTO(kpiEvent));
                        break;
                }
            }
        }
        return eventDTOS;
    }

    private List<EventDTO> convertEventEntityToDTONewSupport(List<KpiEvent> kpiEventEntities) throws IOException {
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiEventEntities)) {
            for (KpiEvent kpiEvent : kpiEventEntities) {
                switch (GroupType.getGroupType(kpiEvent.getGroup().getGroupType().getId())) {
                    case CLUB:
                        eventDTOS.add(convertEventClubEntityToDTO(kpiEvent));
                        break;
                    case SUPPORT:
                        eventDTOS.add(convertNewSupportEntityToDTO(kpiEvent));
                        break;
                    case SEMINAR:
                        eventDTOS.add(convertSeminarEntityToDTO(kpiEvent));
                        break;
                    case TEAM_BUILDING:
                        eventDTOS.add(convertEventTeamBuildingEntityToDTO(kpiEvent));
                        break;
                }
            }
        }
        return eventDTOS;
    }

    public List<EventDTO<EventSeminarDetail>> convertSeminarEventEntityToDTO(List<KpiEvent> kpiEventEntities) throws IOException{
        List<EventDTO<EventSeminarDetail>> eventDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiEventEntities))
            for(KpiEvent kpiEvent : kpiEventEntities){
                eventDTOS.add(convertSeminarEntityToDTO(kpiEvent));
            }
        return eventDTOS;
    }

    @Override
    @Transactional
    public EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException,
            NoSuchFieldException {
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

    @Transactional
    @Override
    public EventDTO createSupportEventNew(EventDTO<List<EventSupportDTO>> eventSupportDTO) throws IOException {
        EventDTO<List<EventSupportDTO>> eventSupportRP = new EventDTO<>();
        List<ErrorDTO> validates = validateSupportEventNew(eventSupportDTO);
        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent eventSupport = convertNewEventSupportDTOToEntityForCreate(eventSupportDTO);
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

            eventSupportRP = convertNewSupportEntityToDTO(eventSupport);

        } else {
            eventSupportRP.setErrorCode(validates.get(0).getErrorCode());
            eventSupportRP.setMessage(validates.get(0).getMessage());
            eventSupportRP.setErrorDTOS(validates);
        }
        return eventSupportRP;
    }

    private List<ErrorDTO> validateSupportEventNew(EventDTO<List<EventSupportDTO>> eventSupportDTO) {
        List<KpiSupport> kpiSupport = (List<KpiSupport>) kpiSupportRepo.findAll();

        List<ErrorDTO> errors = new ArrayList<>();
        if (Objects.isNull(eventSupportDTO.getBeginDate())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.START_DATE_CAN_NOT_NULL);

            errors.add(errorDTO);
        }

        if (Objects.isNull(eventSupportDTO.getGroup())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.GROUP_CAN_NOT_NULL);

            errors.add(errorDTO);
        } else {
            if (Objects.isNull(eventSupportDTO.getGroup().getId())) {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errorDTO.setMessage(ErrorMessage.GROUP_ID_CAN_NOT_NULL);

                errors.add(errorDTO);
            } else {
                Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(eventSupportDTO.getGroup().getId());
                if (!kpiGroup.isPresent() || !Objects.equals(kpiGroup.get().getGroupType().getId(),
                        GroupType.SUPPORT.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(ErrorMessage.GROUP_ID_NOT_IS_SUPPORT);
                    errors.add(errorDTO);
                }
            }
        }

        if (CollectionUtils.isEmpty(eventSupportDTO.getAdditionalConfig())) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.LIST_SUPPORT_CAN_NOT_EMPTY);

            errors.add(errorDTO);
        } else {
            for (int i = 0; i < eventSupportDTO.getAdditionalConfig().size(); i++) {
                EventSupportDTO supportDTO = eventSupportDTO.getAdditionalConfig().get(i);

                if (Objects.isNull(supportDTO.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(String.format(ErrorMessage.ID_TASK_AT_INDEX_CAN_NOT_NULL, i));

                    errors.add(errorDTO);
                } else {
                    if (kpiSupport.stream().noneMatch(kpiSupport1 -> kpiSupport1.getId().equals(supportDTO.getId()))) {
                        ErrorDTO errorDTO = new ErrorDTO();

                        errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                        errorDTO.setMessage(String.format(ErrorMessage.ID_SUPPORT_CAN_NOT_INCORRECT_AT_INDEX, i));

                        errors.add(errorDTO);
                    }
                }
                if (Objects.isNull(supportDTO.getQuantity())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(String.format(ErrorMessage.QUANTITY_AT_INDEX_CAN_NOT_NULL_NEW, i));

                    errors.add(errorDTO);
                } else {
                    if (supportDTO.getQuantity() <= 0) {
                        ErrorDTO errorDTO = new ErrorDTO();

                        errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                        errorDTO.setMessage(String.format(ErrorMessage.QUANTITY_AT_INDEX_CAN_NOT_LESS_ONE_NEW, i));

                        errors.add(errorDTO);
                    }
                }

            }
        }
        Collection<EventSupportDTO> supportDTOS = eventSupportDTO.getAdditionalConfig().stream()
                .collect(Collectors.toConcurrentMap(SupportDTO::getId, Function.identity(), (p, q) -> p))
                .values();
        if (supportDTOS.size() != eventSupportDTO.getAdditionalConfig().size()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.ID_TASK_SUPPORT_CAN_NOT_DUPLICATE);
            errors.add(errorDTO);
        }
        //for update
        if (Objects.nonNull(eventSupportDTO.getId())) {
            Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(eventSupportDTO.getId());

            if (kpiEventOptional.isPresent()) {
                KpiEvent kpiEvent = kpiEventOptional.get();
                if (!Objects.equals(kpiEvent.getGroup().getGroupType().getId(), GroupType.SUPPORT.getId())) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errorDTO.setMessage(ErrorMessage.GROUP_WITH_ID_NOT_IS_SUPPORT);

                    errors.add(errorDTO);
                }
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(eventSupportDTO.getId());
                if (!(kpiEventUsers.size() > 1)
                        && !(kpiEventUsers.get(0).getKpiEventUserPK().getUserName().equals(
                        authentication.getPrincipal()))) {
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

    @Override
    @Transactional
    public EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException,
            NoSuchFieldException {
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
    public EventDTO updateSupportEventNew(EventDTO<List<EventSupportDTO>> supportDTO) throws IOException,
            NoSuchFieldException {
        EventDTO<List<EventSupportDTO>> eventSupportDTO = new EventDTO<>();
        List<ErrorDTO> validates = validateSupportEventNew(supportDTO);
        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent eventSupport = convertNewEventSupportDTOToEntityForUpdate(supportDTO);
            eventSupport = kpiEventRepo.save(eventSupport);
            eventSupportDTO = convertNewSupportEntityToDTO(eventSupport);
        } else {
            eventSupportDTO.setErrorCode(validates.get(0).getErrorCode());
            eventSupportDTO.setMessage(validates.get(0).getMessage());
            eventSupportDTO.setErrorDTOS(validates);
        }
        return eventSupportDTO;
    }

    private KpiEvent convertNewEventSupportDTOToEntityForUpdate(EventDTO<List<EventSupportDTO>> supportDTO) throws
            JsonProcessingException {
        KpiEvent eventSupport;
        ObjectMapper mapper = new ObjectMapper();
        eventSupport = kpiEventRepo.findById(supportDTO.getId()).get();

        eventSupport.setBeginDate(supportDTO.getBeginDate());
        String additionalConfigSupport = mapper.writeValueAsString(supportDTO.getAdditionalConfig());
        eventSupport.setAdditionalConfig(additionalConfigSupport);

        eventSupport.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

        return eventSupport;
    }

    @Override
    public EventDTO createSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException {
        eventDTO.setStatus(StatusEvent.WAITING.getValue());
        EventDTO validateSeminarDTO = new EventDTO();

        List<ErrorDTO> validates = validateDataSeminarEvent(eventDTO);

        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent kpiEvent = new KpiEvent();
            ObjectMapper mapper = new ObjectMapper();

            String seminarEventConfig = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
            BeanUtils.copyProperties(eventDTO, kpiEvent);

            kpiEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(eventDTO.getGroup().getId());

            if (kpiGroup.isPresent()) {
                kpiEvent.setGroup(kpiGroup.get());
                kpiEvent.setAdditionalConfig(kpiGroup.get().getAdditionalConfig());
                kpiEvent = kpiEventRepo.save(kpiEvent);

                List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());

                kpiEventUserRepo.saveAll(eventUsers);

                BeanUtils.copyProperties(kpiEvent, validateSeminarDTO);
                validateSeminarDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));

                for(EventUserDTO eventUserDTO : eventDTO.getEventUserList()){
                    KpiUser kpiUser = kpiUserRepo.findByUserName(eventUserDTO.getUser().getUsername());
                    UserDTO userDTO = convertUserEntityToDTO(kpiUser);
                    eventUserDTO.setUser(userDTO);
                    eventUserDTO.setType(EvaluatingStatus.UNFINISHED.getValue());
                }

                validateSeminarDTO.setEventUserList(eventDTO.getEventUserList());


            } else {
                validateSeminarDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                validateSeminarDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        } else {
            validateSeminarDTO.setErrorCode(validates.get(0).getErrorCode());
            validateSeminarDTO.setMessage(validates.get(0).getMessage());
            validateSeminarDTO.setErrorDTOS(validates);
        }
        return validateSeminarDTO;
    }

    @Override
    @Transactional
    public EventDTO updateSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException {
        kpiEventUserRepo.deleteByEventId(eventDTO.getId());
        EventDTO validateSeminarDTO = new EventDTO();
        Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(eventDTO.getId());
        List<ErrorDTO> validates = validateDataSeminarEvent(eventDTO);
        if (kpiEventOptional.isPresent()) {
            KpiEvent kpiEvent = kpiEventOptional.get();
            List<String> usernameFinishSurvey = kpiEvent.getKpiEventUserList().stream()
                    .filter(u -> u.getStatus() == 1)
                    .map(u -> u.getKpiEventUserPK().getUserName())
                    .collect(Collectors.toList());

            if (Objects.equals(kpiEvent.getStatus(), StatusEvent.WAITING.getValue())) {
                if (CollectionUtils.isEmpty(validates)) {

                    ObjectMapper mapper = new ObjectMapper();

                    String seminarEventConfig = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
                    BeanUtils.copyProperties(eventDTO, kpiEvent, "id", "createdDate", "updatedDate", "group", "status");

                    kpiEvent.setAdditionalConfig(seminarEventConfig);
                    Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());
                    if (kpiGroupOptional.isPresent()) {
                        kpiEvent.setGroup(kpiGroupOptional.get());
                        kpiEvent.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                        List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());
                        kpiEvent.setKpiEventUserList(eventUsers);
                        kpiEvent = kpiEventRepo.save(kpiEvent);

                        for(KpiEventUser eventUser : eventUsers){
                            if(usernameFinishSurvey.stream()
                                                   .anyMatch(u -> u.equals(eventUser.getKpiEventUserPK().getUserName()))){
                                eventUser.setStatus(EvaluatingStatus.FINISH.getValue());
                            }
                        }

                        kpiEventUserRepo.saveAll(eventUsers);

                        BeanUtils.copyProperties(kpiEvent, validateSeminarDTO);
                        validateSeminarDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));
                        validateSeminarDTO.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                        validateSeminarDTO.setAdditionalConfig(eventDTO.getAdditionalConfig());

                        for(EventUserDTO eventUserDTO : eventDTO.getEventUserList()){
                            KpiUser kpiUser = kpiUserRepo.findByUserName(eventUserDTO.getUser().getUsername());
                            UserDTO userDTO = convertUserEntityToDTO(kpiUser);
                            eventUserDTO.setUser(userDTO);
                            if(usernameFinishSurvey.stream()
                                    .anyMatch(u -> u.equals(eventUserDTO.getUser().getUsername()))){
                                eventUserDTO.setStatus(EvaluatingStatus.FINISH.getValue());
                            }else{
                                eventUserDTO.setStatus(EvaluatingStatus.UNFINISHED.getValue());
                            }
                        }

                        validateSeminarDTO.setEventUserList(eventDTO.getEventUserList());

                    } else {
                        validateSeminarDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                        validateSeminarDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    }
                }
            } else {
                validateSeminarDTO.setMessage(ErrorMessage.CAN_NOT_UPDATE_EVENT);
                validateSeminarDTO.setErrorCode(ErrorCode.CANNOT_UPDATE.getValue());
            }

        } else {
            validateSeminarDTO.setMessage(ErrorMessage.NOT_FIND_SEMINAR);
            validateSeminarDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());

            validateSeminarDTO.setErrorCode(validates.get(0).getErrorCode());
            validateSeminarDTO.setMessage(validates.get(0).getMessage());
            validateSeminarDTO.setErrorDTOS(validates);
        }
        return validateSeminarDTO;
    }

    @Override
    public EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException,
            IllegalAccessException {
        Optional<KpiEvent> event = kpiEventRepo.findById(eventDTO.getId());
        if (event.isPresent()) {
            KpiEvent kpiEvent = event.get();
            if (validateYearMonth(kpiEvent.getCreatedDate())) {
                if (Objects.equals(kpiEvent.getStatus(), StatusEvent.WAITING.getValue())) {
                    GroupType groupType = GroupType.getGroupType(kpiEvent.getGroup().getGroupType().getId());
                    switch (groupType) {
                        case CLUB:
                            eventDTO = confirmOrCancelEventClub(kpiEvent, eventDTO);
                            break;
                        case SUPPORT:
                            eventDTO = confirmOrCancelEventSupport(kpiEvent, eventDTO);
                            break;
                    }
                } else {
                    eventDTO.setErrorCode(ErrorCode.DATA_CAN_NOT_CHANGE.getValue());
                    eventDTO.setMessage(ErrorMessage.EVENT_CONFIRMED_OR_CANCELED);
                }
            } else {
                eventDTO.setErrorCode(ErrorCode.DATA_CAN_NOT_CHANGE.getValue());
                eventDTO.setMessage(ErrorMessage.CAN_NOT_CHANGE_STATUS_EVENT_LAST_MONTH);
            }
        } else {
            eventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            eventDTO.setMessage(ErrorMessage.NOT_FIND_EVENT_BY_ID);
        }
        return eventDTO;
    }

    @Override
    public EventDTO createTeamBuildingEvent(EventDTO<EventTeamBuildingDetail> eventDTO) throws IOException {
        EventDTO validateTeambuildingDTO = new EventDTO();
        List<ErrorDTO> validates = validateTeambuildingEvent(eventDTO);

        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent kpiEvent = new KpiEvent();

            BeanUtils.copyProperties(eventDTO, kpiEvent, "createdDate", "updatedDate");

            Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(eventDTO.getGroup().getId());
            if (kpiGroup.isPresent()) {
                KpiGroup kpiGroup1 = kpiGroup.get();
                kpiEvent.setGroup(kpiGroup1);
                if (kpiEvent.getGroup().getGroupType().getId() == GroupType.TEAM_BUILDING.getId()) {
                    kpiEvent.setAdditionalConfig(kpiGroup1.getAdditionalConfig());
                    kpiEvent = kpiEventRepo.save(kpiEvent);

                    List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());
                    kpiEventUserRepo.saveAll(eventUsers);

                    BeanUtils.copyProperties(kpiEvent, validateTeambuildingDTO);
                    validateTeambuildingDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));
                    validateTeambuildingDTO.setEventUserList(eventDTO.getEventUserList());
                    validateTeambuildingDTO.setAdditionalConfig(convertAdditionalConfigToDTO(kpiEvent.getGroup()));

                    pointService.calculateTeambuildingPoint(validateTeambuildingDTO);


                } else {
                    validateTeambuildingDTO.setMessage(ErrorMessage.GROUP_TYPE_IS_INVALID);
                    validateTeambuildingDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                }

            } else {
                validateTeambuildingDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                validateTeambuildingDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        } else {

            validateTeambuildingDTO.setErrorCode(validates.get(0).getErrorCode());
            validateTeambuildingDTO.setMessage(validates.get(0).getMessage());
            validateTeambuildingDTO.setErrorDTOS(validates);
        }

        return validateTeambuildingDTO;
    }

    private List<ErrorDTO> validateTeambuildingEvent(EventDTO<EventTeamBuildingDetail> eventDTO) {
        List<ErrorDTO> errors = new ArrayList<>();

        List<EventUserDTO> firstPlaces = new ArrayList<>();
        List<EventUserDTO> secondPlaces = new ArrayList<>();
        List<EventUserDTO> thirdPlaces = new ArrayList<>();
        List<EventUserDTO> organizer = new ArrayList<>();

        if (Objects.isNull(eventDTO)) {
            ErrorDTO nullEvent = new ErrorDTO();
            nullEvent.setMessage(ErrorMessage.EVENT_CAN_NOT_NULL);
            nullEvent.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(nullEvent);
        }
        if (Objects.isNull(eventDTO.getGroup())) {
            ErrorDTO nullGroup = new ErrorDTO();
            nullGroup.setMessage(ErrorMessage.GROUP_CAN_NOT_NULL);
            nullGroup.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(nullGroup);
        }
        if (Objects.isNull(eventDTO.getEventUserList())) {
            ErrorDTO nullEventUserList = new ErrorDTO();
            nullEventUserList.setMessage(ErrorMessage.EVENT_USER_LIST_CAN_NOT_NULL);
            nullEventUserList.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(nullEventUserList);
        } else {
            for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
                if (Objects.isNull(eventUserDTO)) {
                    ErrorDTO nullUser = new ErrorDTO();
                    nullUser.setMessage(ErrorMessage.EVENT_USER_CAN_NOT_NULL);
                    nullUser.setErrorCode(ErrorCode.NOT_NULL.getValue());
                    errors.add(nullUser);
                }

                if (eventUserDTO.getUser().getUsername() == null) {
                    ErrorDTO userError = new ErrorDTO();
                    userError.setMessage(ErrorMessage.USERNAME_CAN_NOT_NULL);
                    userError.setErrorCode(ErrorCode.NOT_NULL.getValue());
                    errors.add(userError);
                } else if (!validateTeamBuildingUser(eventDTO)) {
                    ErrorDTO userError = new ErrorDTO();
                    userError.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    userError.setMessage(ErrorMessage.NOT_FIND_USER);
                    errors.add(userError);
                }

                ErrorDTO eventUserTypeError = new ErrorDTO();
                if (eventUserDTO.getType() == null) {
                    eventUserTypeError.setMessage(ErrorMessage.MEMBER_TYPE_CAN_NOT_NULL);
                    eventUserTypeError.setErrorCode(ErrorCode.NOT_NULL.getValue());
                    errors.add(eventUserTypeError);
                } else if ((eventUserDTO.getType() < EventUserType.ORGANIZER.getValue())
                        || (eventUserDTO.getType() > EventUserType.THIRD_PLACE.getValue())) {
                    eventUserTypeError.setMessage(ErrorMessage.INVALIDATED_MEMBER_TYPE);
                    eventUserTypeError.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    errors.add(eventUserTypeError);
                } else {
                    if (eventUserDTO.getType() == EventUserType.FIRST_PLACE.getValue()) {
                        firstPlaces.add(eventUserDTO);
                    } else if (eventUserDTO.getType() == EventUserType.SECOND_PLACE.getValue()) {
                        secondPlaces.add(eventUserDTO);
                    } else if (eventUserDTO.getType() == EventUserType.THIRD_PLACE.getValue()) {
                        thirdPlaces.add(eventUserDTO);
                    } else {
                        organizer.add(eventUserDTO);
                    }
                }
            }
        }

        if (firstPlaces.size() == 0) {
            ErrorDTO nullFirstPlaceError = new ErrorDTO();
            nullFirstPlaceError.setErrorCode(ErrorCode.NOT_NULL.getValue());
            nullFirstPlaceError.setMessage(ErrorMessage.FIRST_PLACE_CAN_NOT_NULL);
            errors.add(nullFirstPlaceError);
        }
        if (secondPlaces.size() == 0) {
            ErrorDTO nullSecondPlaceError = new ErrorDTO();
            nullSecondPlaceError.setErrorCode(ErrorCode.NOT_NULL.getValue());
            nullSecondPlaceError.setMessage(ErrorMessage.SECOND_PLACE_CAN_NOT_NULL);
            errors.add(nullSecondPlaceError);
        }
        if (thirdPlaces.size() == 0) {
            ErrorDTO nullThirdPlaceError = new ErrorDTO();
            nullThirdPlaceError.setErrorCode(ErrorCode.NOT_NULL.getValue());
            nullThirdPlaceError.setMessage(ErrorMessage.THIRD_PLACE_CAN_NOT_NULL);
            errors.add(nullThirdPlaceError);
        }
        if (organizer.size() == 0) {
            ErrorDTO nullOrganizer = new ErrorDTO();
            nullOrganizer.setErrorCode(ErrorCode.NOT_NULL.getValue());
            nullOrganizer.setMessage(ErrorMessage.ORGANIZER_CAN_NOT_NULL);
            errors.add(nullOrganizer);
        }
        if (eventDTO.getName() == null) {
            ErrorDTO nullEventName = new ErrorDTO();
            nullEventName.setMessage(ErrorMessage.EVENT_NAME_CAN_NOT_NULL);
            nullEventName.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(nullEventName);
        }
        if (eventDTO.getBeginDate() == null) {
            ErrorDTO nullBeginDate = new ErrorDTO();
            nullBeginDate.setMessage(ErrorMessage.BEGIN_DATE_CAN_NOT_NULL);
            nullBeginDate.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(nullBeginDate);
        }
        return errors;
    }

    @Override
    public EventDTO createSeminarSurvey(EventDTO<List<SeminarSurveyDTO>> seminarSurveyDTO) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        EventDTO<EventSeminarDetail> seminarDetailEventDTO = new EventDTO<>();

        String loginUsername = authentication.getPrincipal().toString();

        Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(seminarSurveyDTO.getId());
        List<KpiEventUser> kpiEventHostList = kpiEventUserRepo.findByKpiEventId(seminarSurveyDTO.getId());
        List<UserDTO> employee = userService.getAllEmployee();

        List<ErrorDTO> errors = new ArrayList<>();

        if (kpiEventOptional.isPresent()) {
            KpiEvent kpiEvent = kpiEventOptional.get();
            List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());
            seminarDetailEventDTO = convertEventSeminarEntityToDTO(kpiEvent);

            Optional<KpiEventUser> kpiEventUserOptional = kpiEventHostList.stream()
                    .filter(kpiEventUser -> kpiEventUser.getKpiUser().getUserName().equals(loginUsername)).findFirst();

            if (kpiEventUserOptional.isPresent()) {
                KpiEventUser kpiEventUser = kpiEventUserOptional.get();
                if (!kpiEventUser.getType().equals(EventUserType.HOST.getValue())) {
                    if (kpiEventUser.getStatus().equals(EvaluatingStatus.UNFINISHED.getValue())) {
                        List<KpiSeminarSurvey> kpiSeminarSurveys = new ArrayList<>();

                        List<KpiEventUser> eventUserEvaluated = kpiEventHostList.stream()
                                .filter(value -> !value.getKpiUser().getUserName().equals(loginUsername))
                                .collect(Collectors.toList());

                        for (SeminarSurveyDTO RequestSeminarSurveyDTO : seminarSurveyDTO.getAdditionalConfig()) {
                            KpiSeminarSurvey kpiSeminarSurvey = new KpiSeminarSurvey();

                            Optional<KpiEventUser> kpiEventUserEvaluated = eventUserEvaluated.stream()
                                    .filter(kpiEventUser1 -> kpiEventUser1.getKpiUser().getUserName()
                                            .equals(RequestSeminarSurveyDTO.getEvaluatedUsername().getUsername())
                                            && kpiEventUser1.getType().equals(EventUserType.HOST.getValue()))
                                    .findFirst();
                            if (kpiEventUserEvaluated.isPresent()) {
                                kpiSeminarSurvey.setEvaluatedUsername(kpiEventUserEvaluated.get().getKpiUser());
                                kpiSeminarSurvey.setEvaluatingUsername(kpiEventUser.getKpiUser());
                                kpiSeminarSurvey.setEvent(kpiEvent);
                                kpiSeminarSurvey.setRating(RequestSeminarSurveyDTO.getRating());

                                kpiSeminarSurveys.add(kpiSeminarSurvey);
                            } else {
                                ErrorDTO errorDTO = new ErrorDTO();
                                errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                                errorDTO.setMessage(ErrorMessage.HOST_DOES_NOT_EXIST);
                                errors.add(errorDTO);
                            }
                        }
                        if (CollectionUtils.isEmpty(errors)) {
                            List<SeminarSurveyDTO> seminarSurveyDTOs = convertListSeminarSurveyEntityToDTO
                                    ((List<KpiSeminarSurvey>) kpiseminarSurveyRepo.saveAll(kpiSeminarSurveys));
                            kpiEventUser.setStatus(EvaluatingStatus.FINISH.getValue());
                            kpiEventUserRepo.save(kpiEventUser);
                            EventUserDTO eventUserDTO = convertEventUsersEntityToDTONotHaveEvent(kpiEventUser);
                            eventUserDTO.setSeminarSurveys(seminarSurveyDTOs);
                            seminarDetailEventDTO.setEventUserList(Lists.newArrayList(eventUserDTO));
                        }

                        if(kpiEventUsers.stream()
                                .noneMatch(e -> (e.getType().equals(EventUserType.MEMBER.getValue()) || e.getType().equals(EventUserType.LISTEN.getValue()))
                                        && e.getStatus().equals(EvaluatingStatus.UNFINISHED.getValue()))){
                            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
                            kpiEventRepo.save(kpiEvent);
                            pointService.addSeminarPoint(kpiEventUsers, seminarDetailEventDTO);
                        }

                    } else {
                        ErrorDTO errorDTO = new ErrorDTO();
                        errorDTO.setErrorCode(ErrorCode.ALREADY_EVALUATED.getValue());
                        errorDTO.setMessage(ErrorCode.ALREADY_EVALUATED.getDescription());
                        errors.add(errorDTO);
                    }

                } else {
                    ErrorDTO errorDTO = new ErrorDTO();
                    errorDTO.setErrorCode(ErrorCode.HOST_CANNOT_CREATE_SEMINAR_SURVEY.getValue());
                    errorDTO.setMessage(ErrorMessage.HOST_CANNOT_CREATE_SEMINAR_SURVEY);
                    errors.add(errorDTO);
                }
            } else {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.NOT_ATTEND_EVENT.getValue());
                errorDTO.setMessage(ErrorCode.NOT_ATTEND_EVENT.getDescription());
                errors.add(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            errorDTO.setMessage(ErrorMessage.NOT_FIND_EVENT);
            errors.add(errorDTO);
        }

        if (!CollectionUtils.isEmpty(errors)) {
            seminarDetailEventDTO.setErrorCode(errors.get(0).getErrorCode());
            seminarDetailEventDTO.setMessage(errors.get(0).getMessage());
            seminarDetailEventDTO.setErrorDTOS(errors);
        }

        return seminarDetailEventDTO;
    }

    private EventUserDTO convertEventUsersEntityToDTONotHaveEvent(KpiEventUser kpiEventUser) {
        EventUserDTO eventUserDTO = new EventUserDTO();
        eventUserDTO.setUser(convertUserEntityToDTO(kpiEventUser.getKpiUser()));
        eventUserDTO.setStatus(EvaluatingStatus.FINISH.getValue());
        return eventUserDTO;
    }

    private List<SeminarSurveyDTO> convertListSeminarSurveyEntityToDTO(List<KpiSeminarSurvey> kpiSeminarSurveys) {
        List<SeminarSurveyDTO> seminarSurveyDTOS = new ArrayList<>();

        for (KpiSeminarSurvey kpiSeminarSurvey : kpiSeminarSurveys) {
            SeminarSurveyDTO seminarSurveyDTO = new SeminarSurveyDTO();
            seminarSurveyDTO.setRating(kpiSeminarSurvey.getRating());
            seminarSurveyDTO.setEvaluatedUsername(convertUserEntityToDTO(kpiSeminarSurvey.getEvaluatedUsername()));
            seminarSurveyDTOS.add(seminarSurveyDTO);
        }
        return seminarSurveyDTOS;
    }

    private UserDTO convertUserEntityToDTO(KpiUser user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setUsername(user.getUserName());
        return userDTO;
    }

    @Override
    public EventDTO confirmOrCancelEventSupportNew(EventDTO eventDTO) throws IOException, NoSuchFieldException,
            IllegalAccessException {
        Optional<KpiEvent> event = kpiEventRepo.findById(eventDTO.getId());
        if (event.isPresent()) {
            KpiEvent kpiEvent = event.get();
            if (validateYearMonth(kpiEvent.getCreatedDate())) {
                if (Objects.equals(kpiEvent.getStatus(), StatusEvent.WAITING.getValue())) {
                    GroupType groupType = GroupType.getGroupType(kpiEvent.getGroup().getGroupType().getId());
                    switch (groupType) {
                        case CLUB:
                            eventDTO = confirmOrCancelEventClub(kpiEvent, eventDTO);
                            break;
                        case SUPPORT:
                            eventDTO = confirmOrCancelEventSupportNew(kpiEvent, eventDTO);
                            break;
                    }
                } else {
                    eventDTO.setErrorCode(ErrorCode.DATA_CAN_NOT_CHANGE.getValue());
                    eventDTO.setMessage(ErrorMessage.EVENT_CONFIRMED_OR_CANCELED);
                }
            } else {
                eventDTO.setErrorCode(ErrorCode.DATA_CAN_NOT_CHANGE.getValue());
                eventDTO.setMessage(ErrorMessage.CAN_NOT_CHANGE_STATUS_EVENT_LAST_MONTH);
            }
        } else {
            eventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            eventDTO.setMessage(ErrorMessage.NOT_FIND_EVENT_BY_ID);
        }
        return eventDTO;
    }

    private EventDTO confirmOrCancelEventClub(KpiEvent kpiEvent, EventDTO eventDTO) throws IOException {
        if (Objects.equals(eventDTO.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
        } else {
            kpiEvent.setStatus(StatusEvent.CANCEL.getValue());
        }
        kpiEvent.setAdditionalConfig(kpiEvent.getGroup().getAdditionalConfig());

        kpiEvent = kpiEventRepo.save(kpiEvent);
        if (Objects.equals(kpiEvent.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            addPointWhenConfirmClub(kpiEvent);
        }
        eventDTO = convertEventClubEntityToDTO(kpiEvent);
        return eventDTO;
    }

    private void addPointWhenConfirmClub(KpiEvent kpiEvent) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupClubDetail groupClubDetail = mapper.readValue(kpiEvent.getGroup().getAdditionalConfig(),
                GroupClubDetail.class);
        List<UserDTO> employee = userService.getAllEmployee();
        for (KpiEventUser kpiEventUser : kpiEvent.getKpiEventUserList()) {
            if(isEmployee(kpiEventUser.getKpiUser(), employee)){
                addClubPoint(kpiEventUser.getKpiUser(), groupClubDetail.getParticipationPoint(), kpiEvent);
            }
        }
    }

    private boolean validateYearMonth(java.util.Date yearMonth) {
        //is true if in month old , is false if la new
        Timestamp dateCun = new Timestamp(System.currentTimeMillis());

        if (dateCun.getYear() + 1900 == yearMonth.getYear() + 1900
                && dateCun.getMonth() + 1 == yearMonth.getMonth() + 1
                ) {
            return true;
        }
        if (dateCun.getYear() + 1900 == yearMonth.getYear() + 1900
                && (dateCun.getMonth() + 1 )- (yearMonth.getMonth() + 1) == 1
                && dateCun.getDate() > Integer.valueOf(
                environment.getProperty("config.day.new.year.month"))
                && dateCun.getHours() < Integer.valueOf(
                environment.getProperty("config.hour.new.year.month"))
                ) {
            return true;
        }
        return false;
    }

    private EventDTO confirmOrCancelEventSupport(KpiEvent kpiEvent, EventDTO eventDTO) throws IOException,
            NoSuchFieldException,
            IllegalAccessException {
        if (Objects.equals(eventDTO.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
        } else {
            kpiEvent.setStatus(StatusEvent.CANCEL.getValue());
        }
        Float point = setHistorySupportAndGetAllPoint(kpiEvent);
        //ad point
        if (Objects.equals(kpiEvent.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            List<UserDTO> employee = userService.getAllEmployee();
            if(isEmployee(kpiEvent.getKpiEventUserList().get(0).getKpiUser(), employee)){
                addSupportPoint(kpiEvent.getKpiEventUserList().get(0).getKpiUser(), point, kpiEvent);
            }
        }
        kpiEvent = kpiEventRepo.save(kpiEvent);
        eventDTO = convertSupportEntiyToDTO(kpiEvent);
        return eventDTO;
    }

    private EventDTO confirmOrCancelEventSupportNew(KpiEvent kpiEvent, EventDTO eventDTO) throws IOException,
            NoSuchFieldException,
            IllegalAccessException {
        if (Objects.equals(eventDTO.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            kpiEvent.setStatus(StatusEvent.CONFIRMED.getValue());
        } else {
            kpiEvent.setStatus(StatusEvent.CANCEL.getValue());
        }
        Float point = setHistorySupportAndGetAllPointNewSupport(kpiEvent);
        //ad point
        if (Objects.equals(kpiEvent.getStatus(), StatusEvent.CONFIRMED.getValue())) {
            List<UserDTO> employee = userService.getAllEmployee();
            if(isEmployee(kpiEvent.getKpiEventUserList().get(0).getKpiUser(), employee)){
                addSupportPoint(kpiEvent.getKpiEventUserList().get(0).getKpiUser(), point, kpiEvent);
            }
        }
        kpiEvent = kpiEventRepo.save(kpiEvent);
        eventDTO = convertNewSupportEntityToDTO(kpiEvent);
        return eventDTO;
    }

    private Float setHistorySupportAndGetAllPoint(KpiEvent kpiEvent) throws NoSuchFieldException,
            IllegalAccessException,
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

    private Float setHistorySupportAndGetAllPointNewSupport(KpiEvent kpiEvent) throws NoSuchFieldException,
            IllegalAccessException,
            IOException {
        ObjectMapper mapper = new ObjectMapper();
        Float point = 0F;
        TypeReference<List<EventSupportDTO>> tRefBedType = new TypeReference<List<EventSupportDTO>>() {
        };
        List<EventSupportDTO> configEventSupport = mapper.readValue(kpiEvent.getAdditionalConfig(), tRefBedType);
        kpiEvent.setAdditionalConfig(mapper.writeValueAsString(configEventSupport));

        List<KpiSupport> kpiSupports = (List<KpiSupport>) kpiSupportRepo.findAll();
        for (EventSupportDTO eventSupportDetail : configEventSupport) {
            Optional<KpiSupport> supportOptional = kpiSupports.stream().filter(
                    kpiSupportFilter -> kpiSupportFilter.getId().equals(eventSupportDetail.getId())).findFirst();
            KpiSupport kpiSupport1 = supportOptional.get();
            BeanUtils.copyProperties(kpiSupport1, eventSupportDetail, "quantity");
            point += eventSupportDetail.getPoint() * eventSupportDetail.getQuantity();

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

        BeanUtils.copyProperties(kpiEvent, eventSupportDTO);

        TypeReference<List<EventSupportDetail>> tRefBedType = new TypeReference<List<EventSupportDetail>>() {
        };
        detailsConfigSupport = mapper.readValue(kpiEvent.getAdditionalConfig(), tRefBedType);
        eventSupportDTO.setAdditionalConfig(detailsConfigSupport);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        List<EventUserDTO> eventUserDTOS = convertListEventUserEntityToDTO(kpiEventUsers);
        eventSupportDTO.setEventUserList(eventUserDTOS);
        eventSupportDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));
        eventSupportDTO.setCreator(convertCreatorToDTO(kpiEvent.getCreator()));

        return eventSupportDTO;
    }

    private EventDTO<List<EventSupportDTO>> convertNewSupportEntityToDTO(KpiEvent kpiEvent) throws IOException {
        EventDTO<List<EventSupportDTO>> eventSupportDTO = new EventDTO<>();
        List<EventSupportDTO> detailsConfigSupport;
        ObjectMapper mapper = new ObjectMapper();

        BeanUtils.copyProperties(kpiEvent, eventSupportDTO);

        TypeReference<List<EventSupportDTO>> tRefBedType = new TypeReference<List<EventSupportDTO>>() {
        };
        detailsConfigSupport = mapper.readValue(kpiEvent.getAdditionalConfig(), tRefBedType);
        eventSupportDTO.setAdditionalConfig(detailsConfigSupport);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        List<EventUserDTO> eventUserDTOS = convertListEventUserEntityToDTO(kpiEventUsers);
        eventSupportDTO.setEventUserList(eventUserDTOS);
        eventSupportDTO.setGroup(convertConfigEventToDTONewSupport(kpiEvent.getGroup()));

        return eventSupportDTO;
    }

    private EventDTO convertEventClubEntityToDTO(KpiEvent kpiEvent) throws IOException {
        EventDTO<EventClubDetail> clubDetailEventDTO = new EventDTO<>();
        EventClubDetail eventClubDetail;
        ObjectMapper mapper = new ObjectMapper();

        BeanUtils.copyProperties(kpiEvent, clubDetailEventDTO);

        eventClubDetail = mapper.readValue(kpiEvent.getAdditionalConfig(), EventClubDetail.class);
        clubDetailEventDTO.setAdditionalConfig(eventClubDetail);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        List<EventUserDTO> eventUserDTOS = convertListEventUserEntityToDTO(kpiEventUsers);
        clubDetailEventDTO.setEventUserList(eventUserDTOS);
        clubDetailEventDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));
        clubDetailEventDTO.setCreator(convertCreatorToDTO(kpiEvent.getCreator()));

        return clubDetailEventDTO;
    }

    public EventDTO<EventSeminarDetail> convertSeminarEntityToDTO(KpiEvent kpiEvent) throws IOException {
        EventDTO<EventSeminarDetail> seminarDetailEventDTO = new EventDTO<>();
        ObjectMapper mapper = new ObjectMapper();

        BeanUtils.copyProperties(kpiEvent, seminarDetailEventDTO);
        EventSeminarDetail eventSeminarDetail = mapper.readValue(kpiEvent.getAdditionalConfig(),
                EventSeminarDetail.class);
        seminarDetailEventDTO.setAdditionalConfig(eventSeminarDetail);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        List<EventUserDTO> eventUserDTOS = convertListEventUserEntityToDTO(kpiEventUsers);
        seminarDetailEventDTO.setEventUserList(eventUserDTOS);
        seminarDetailEventDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));

        return seminarDetailEventDTO;
    }

    private EventDTO convertEventTeamBuildingEntityToDTO(KpiEvent kpiEvent) throws IOException {
        EventDTO<EventTeamBuildingDetail> teamBuildingEventDTO = new EventDTO<>();
        EventTeamBuildingDetail eventTeamBuildingDetail;
        ObjectMapper mapper = new ObjectMapper();

        BeanUtils.copyProperties(kpiEvent, teamBuildingEventDTO);
        eventTeamBuildingDetail = mapper.readValue(kpiEvent.getAdditionalConfig(), EventTeamBuildingDetail.class);
        teamBuildingEventDTO.setAdditionalConfig(eventTeamBuildingDetail);

        List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(kpiEvent.getId());

        List<EventUserDTO> eventUserDTOS = convertListEventUserEntityToDTO(kpiEventUsers);
        teamBuildingEventDTO.setEventUserList(eventUserDTOS);
        teamBuildingEventDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));

        return teamBuildingEventDTO;
    }

    private UserDTO convertCreatorToDTO(KpiUser kpiUser) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(kpiUser, userDTO);
        userDTO.setUsername(kpiUser.getUserName());
        return userDTO;
    }

    private GroupDTO convertConfigEventToDTO(KpiGroup kpiGroup) throws IOException {
        GroupDTO groupDTO = new GroupDTO<>();
        ObjectMapper mapper = new ObjectMapper();

        switch (GroupType.getGroupType(kpiGroup.getGroupType().getId())) {
            case SUPPORT:

                BeanUtils.copyProperties(kpiGroup, groupDTO);
                GroupSupportDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        GroupSupportDetail.class);

                groupDTO.setAdditionalConfig(groupSeminarDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
            case CLUB:

                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventClubDetail eventClubDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventClubDetail.class);

                groupDTO.setAdditionalConfig(eventClubDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
            case SEMINAR:

                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventSeminarDetail eventSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventSeminarDetail.class);

                groupDTO.setAdditionalConfig(eventSeminarDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
            case TEAM_BUILDING:
                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventTeamBuildingDetail eventTeamBuildingDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventTeamBuildingDetail.class);

                groupDTO.setAdditionalConfig(eventTeamBuildingDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
        }

        return groupDTO;
    }

    private EventTeamBuildingDetail convertAdditionalConfigToDTO(KpiGroup kpiGroup) throws IOException {
        GroupDTO groupDTO = new GroupDTO<>();
        ObjectMapper mapper = new ObjectMapper();

        BeanUtils.copyProperties(kpiGroup, groupDTO);
        EventTeamBuildingDetail eventTeamBuildingDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                EventTeamBuildingDetail.class);

        return eventTeamBuildingDetail;
    }

    private GroupDTO convertConfigEventToDTONewSupport(KpiGroup kpiGroup) throws IOException {
        GroupDTO groupDTO = new GroupDTO<>();
        ObjectMapper mapper = new ObjectMapper();

        switch (GroupType.getGroupType(kpiGroup.getGroupType().getId())) {
            case SUPPORT:
                List<KpiSupport> kpiSupports = (List<KpiSupport>) kpiSupportRepo.findAll();
                BeanUtils.copyProperties(kpiGroup, groupDTO, "additionalConfig", "groupType");
                List<SupportDTO> supportDTOS = new ArrayList<>();
                for (KpiSupport kpiSupport : kpiSupports) {
                    SupportDTO supportDTO = new SupportDTO();
                    BeanUtils.copyProperties(kpiSupport, supportDTO);
                    supportDTOS.add(supportDTO);
                }
                groupDTO.setAdditionalConfig(supportDTOS);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
            case CLUB:

                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventClubDetail eventClubDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventClubDetail.class);

                groupDTO.setAdditionalConfig(eventClubDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
            case SEMINAR:

                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventSeminarDetail eventSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventSeminarDetail.class);

                groupDTO.setAdditionalConfig(eventSeminarDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
            case TEAM_BUILDING:
                BeanUtils.copyProperties(kpiGroup, groupDTO);
                EventTeamBuildingDetail eventTeamBuildingDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                        EventTeamBuildingDetail.class);

                groupDTO.setAdditionalConfig(eventTeamBuildingDetail);
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                break;
        }

        return groupDTO;
    }

    private List<EventUserDTO> convertListEventUserEntityToDTO(List<KpiEventUser> kpiEventUsers) {
        List<EventUserDTO> eventUserDTOS = new ArrayList<>();

        if (!CollectionUtils.isEmpty(kpiEventUsers)) {
            List<String> namesUser = kpiEventUsers.stream().map(
                    kpiEventUser -> kpiEventUser.getKpiEventUserPK().getUserName())
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
                    eventUserDTO.setStatus(kpiEventUser.getStatus());

                    eventUserDTOS.add(eventUserDTO);
                }
            }
        }
        return eventUserDTOS;
    }

    private EventDTO<EventSeminarDetail> convertEventSeminarEntityToDTO(KpiEvent kpiEvent) throws IOException {

        EventDTO<EventSeminarDetail> eventSeminar = new EventDTO<>();
        ObjectMapper mapper = new ObjectMapper();
        BeanUtils.copyProperties(kpiEvent, eventSeminar);
        eventSeminar.setStatus(kpiEvent.getStatus());
        eventSeminar.setBeginDate(kpiEvent.getBeginDate());
        Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(kpiEvent.getGroup().getId());
        if (kpiGroup.isPresent()) {
            KpiGroup kpiGroupDB = kpiGroup.get();
            EventSeminarDetail additionalConfigSupport = mapper.readValue(kpiEvent.getGroup().getAdditionalConfig(),
                    EventSeminarDetail.class);
            eventSeminar.setAdditionalConfig(additionalConfigSupport);
            eventSeminar.setName(kpiGroupDB.getName());
        }
        return eventSeminar;
    }

    private GroupTypeDTO convertGroupTypeEntityToDTO(KpiGroupType kpiGroupType) {
        GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
        BeanUtils.copyProperties(kpiGroupType, groupTypeDTO);
        return groupTypeDTO;
    }

    private KpiEvent convertEventSupportDTOToEntityForCreate(EventDTO<List<EventSupportDetail>> supportDTO) throws
            JsonProcessingException {
        KpiEvent eventSupport = new KpiEvent();
        ObjectMapper mapper = new ObjectMapper();

        eventSupport.setStatus(StatusEvent.WAITING.getValue());
        eventSupport.setBeginDate(supportDTO.getBeginDate());
        Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(supportDTO.getGroup().getId());
        Optional<KpiUser> userOptional = Optional.ofNullable(kpiUserRepo.findByUserName(supportDTO.getCreator().getUsername()));
        if (kpiGroup.isPresent() && userOptional.isPresent()) {
            KpiGroup kpiGroupDB = kpiGroup.get();
            kpiGroup.ifPresent(eventSupport::setGroup);
            eventSupport.setGroup(kpiGroupDB);
            eventSupport.setCreator(userOptional.get());

            String additionalConfigSupport = mapper.writeValueAsString(supportDTO.getAdditionalConfig());
            eventSupport.setAdditionalConfig(additionalConfigSupport);
            eventSupport.setName(kpiGroupDB.getName());
        }

        return eventSupport;
    }

    private KpiEvent convertNewEventSupportDTOToEntityForCreate(EventDTO<List<EventSupportDTO>> supportDTO) throws
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
                if (!kpiGroup.isPresent() || !Objects.equals(kpiGroup.get().getGroupType().getId(),
                        GroupType.SUPPORT.getId())) {
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
                        && !(kpiEventUsers.get(0).getKpiEventUserPK().getUserName().equals(
                        authentication.getPrincipal()))) {
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

    @Override
    @Transactional
    public EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws IOException {
        eventDTO.setStatus(StatusEvent.WAITING.getValue());

        EventDTO<EventClubDetail> validatedEventDTO = new EventDTO<>();

        KpiEvent kpiEvent = new KpiEvent();

        ObjectMapper mapper = new ObjectMapper();

        List<ErrorDTO> validates = validateClub(eventDTO);

        String loginUsername = eventDTO.getCreator().getUsername();

        if (CollectionUtils.isEmpty(validates)) {
            String clubJson = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
            BeanUtils.copyProperties(eventDTO, kpiEvent);

            kpiEvent.setAdditionalConfig(clubJson);
            Optional<KpiGroup> groupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());
            Optional<KpiUser> userOptional = Optional.ofNullable(kpiUserRepo.findByUserName(loginUsername));

            if (groupOptional.isPresent() && userOptional.isPresent()) {
                kpiEvent.setGroup(groupOptional.get());
                kpiEvent.setCreator(userOptional.get());    //save creator to event
                kpiEvent = kpiEventRepo.save(kpiEvent);

                List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());
                kpiEventUserRepo.saveAll(eventUsers);

                BeanUtils.copyProperties(kpiEvent, validatedEventDTO);
                validatedEventDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));

                for(EventUserDTO eventUserDTO : eventDTO.getEventUserList()){
                    KpiUser kpiUser = kpiUserRepo.findByUserName(eventUserDTO.getUser().getUsername());
                    UserDTO userDTO = convertUserEntityToDTO(kpiUser);
                    eventUserDTO.setUser(userDTO);
                }

                validatedEventDTO.setEventUserList(eventDTO.getEventUserList());
                validatedEventDTO.setCreator(convertCreatorToDTO(kpiEvent.getCreator()));
            } else {
                validatedEventDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
                validatedEventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        } else {
            validatedEventDTO.setErrorCode(validates.get(0).getErrorCode());
            validatedEventDTO.setMessage(validates.get(0).getMessage());
            validatedEventDTO.setErrorDTOS(validates);
        }
        return validatedEventDTO;
    }

    @Override
    public EventDTO updateClub(EventDTO<EventClubDetail> eventDTO) throws IOException {
        kpiEventUserRepo.deleteByEventId(eventDTO.getId());
        EventDTO<EventClubDetail> validatedEventDTO = new EventDTO<>();

        List<ErrorDTO> validates = validateClub(eventDTO);

        Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(eventDTO.getId());

        if (kpiEventOptional.isPresent()) {

            KpiEvent kpiEvent = kpiEventOptional.get();

            if (StatusEvent.WAITING.getValue().equals(kpiEvent.getStatus())) {

                if (CollectionUtils.isEmpty(validates)) {
                    eventDTO.setId(kpiEvent.getId());
                    eventDTO.setStatus(kpiEvent.getStatus());

                    ObjectMapper mapper = new ObjectMapper();
                    String clubJson = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
                    BeanUtils.copyProperties(eventDTO, kpiEvent, "createdDate", "updatedDate", "status");

                    kpiEvent.setAdditionalConfig(clubJson);
                    Optional<KpiGroup> groupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());

                    if (groupOptional.isPresent()) {
                        kpiEvent.setGroup(groupOptional.get());
                        kpiEvent.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                        kpiEvent = kpiEventRepo.save(kpiEvent);

                        List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent,
                                eventDTO.getEventUserList());
                        kpiEventUserRepo.saveAll(eventUsers);

                        BeanUtils.copyProperties(kpiEvent, validatedEventDTO);
                        validatedEventDTO.setGroup(convertConfigEventToDTO(kpiEvent.getGroup()));
                        validatedEventDTO.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                        validatedEventDTO.setAdditionalConfig(eventDTO.getAdditionalConfig());

                        for(EventUserDTO eventUserDTO : eventDTO.getEventUserList()){
                            KpiUser kpiUser = kpiUserRepo.findByUserName(eventUserDTO.getUser().getUsername());
                            UserDTO userDTO = convertUserEntityToDTO(kpiUser);
                            eventUserDTO.setUser(userDTO);
                        }

                        validatedEventDTO.setEventUserList(eventDTO.getEventUserList());

                    } else {
                        validatedEventDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
                        validatedEventDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    }
                } else {
                    validatedEventDTO.setErrorCode(validates.get(0).getErrorCode());
                    validatedEventDTO.setMessage(validates.get(0).getMessage());
                    validatedEventDTO.setErrorDTOS(validates);
                }
            } else {
                validatedEventDTO.setMessage(ErrorMessage.CAN_NOT_UPDATE_EVENT);
                validatedEventDTO.setErrorCode(ErrorCode.CANNOT_UPDATE.getValue());
            }

        }

        return validatedEventDTO;
    }

    public List<KpiEventUser> convertEventUsersToEntity(KpiEvent kpiEvent, List<EventUserDTO> eventUserList) {
        List<KpiEventUser> kpiEventUsers = new ArrayList<>();

        for (EventUserDTO eventUserDTO : eventUserList) {
            KpiEventUser kpiEventUser = new KpiEventUser();
            KpiEventUserPK kpiEventUserPK = new KpiEventUserPK();
            userService.registerUser(eventUserDTO.getUser().getUsername());

            kpiEventUserPK.setEventId(kpiEvent.getId());
            kpiEventUserPK.setUserName(eventUserDTO.getUser().getUsername());
            kpiEventUser.setKpiEventUserPK(kpiEventUserPK);

            kpiEventUser.setType(eventUserDTO.getType());
            kpiEventUser.setStatus(EvaluatingStatus.UNFINISHED.getValue());
            kpiEventUsers.add(kpiEventUser);

        }
        return kpiEventUsers;
    }

    private List<ErrorDTO> validateClub(EventDTO<EventClubDetail> eventDTO) {
        List<ErrorDTO> errors = new ArrayList<>();

        if (eventDTO.getName() == null) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errorDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);

            errors.add(errorDTO);
        }

        if (eventDTO.getBeginDate() == null) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setMessage(ErrorMessage.BEGIN_DATE_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());

            errors.add(errorDTO);

        } else if (eventDTO.getEndDate() == null) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setMessage(ErrorMessage.END_DATE_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());

            errors.add(errorDTO);

        } else {
            if (eventDTO.getBeginDate().after(eventDTO.getEndDate())) {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setMessage(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getDescription());
                errorDTO.setErrorCode(ErrorCode.BEGIN_DATE_IS_NOT_AFTER_END_DATE.getValue());

                errors.add(errorDTO);
            }
        }

        if (eventDTO.getEventUserList().size() == 0) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setMessage(ErrorMessage.LIST_OF_PARTICIPANTS_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());

            errors.add(errorDTO);
        }

        if (!kpiGroupRepo.findById(eventDTO.getGroup().getId()).isPresent()) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
            errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());

            errors.add(errorDTO);
        } else if (eventDTO.getEventUserList().size() != 0) {
            for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
                Integer userType = eventUserDTO.getType();
                if (userType == null) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                    errorDTO.setMessage(ErrorMessage.USER_TYPE_CAN_NOT_NULL);

                    errors.add(errorDTO);
                } else if (userType < EventUserType.HOST.getValue() || userType > EventUserType.LISTEN.getValue()) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    errorDTO.setMessage(ErrorMessage.MEMBER_TYPE_DOES_NOT_EXIST);

                    errors.add(errorDTO);
                } else if (!validateUser(eventDTO)) {
                    ErrorDTO errorDTO = new ErrorDTO();

                    errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    errorDTO.setMessage(ErrorMessage.USER_DOES_NOT_EXIST);

                    errors.add(errorDTO);
                }
            }
        }

        //Validate update
        if (Objects.nonNull(eventDTO.getId())) {
            Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(eventDTO.getId());

            if (!kpiEventOptional.isPresent()) {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                errorDTO.setMessage(ErrorMessage.NOT_FIND_EVENT);

                errors.add(errorDTO);
            }
        }

        return errors;
    }

    private Boolean validateUser(EventDTO<EventClubDetail> eventDTO) {
        List<UserDTO> ldapUserList = ldapUserService.getAllUsers();
        List<UserDTO> ldapUserListClone = new ArrayList<>(ldapUserList);
        List<KpiUser> dbUserList = (List<KpiUser>) kpiUserRepo.findAll();

        ldapUserList.removeIf(userDTO -> dbUserList.stream()
                .anyMatch(kpiUser -> kpiUser.getUserName().equals(userDTO.getUsername())));

        ldapUserList.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
            if (ldapUserListClone.stream().noneMatch(
                    userDTO -> userDTO.getUsername().equals(eventUserDTO.getUser().getUsername()))) {
                return false;
            }
        }
        return true;
    }

    private List<ErrorDTO> validateDataSeminarEvent(EventDTO<EventSeminarDetail> eventDTO) {
        List<ErrorDTO> errors = new ArrayList<>();
        ErrorDTO errorDTO = new ErrorDTO();

        if (Objects.isNull(eventDTO)) {
            errorDTO.setMessage(ErrorMessage.EVENT_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(errorDTO);
        }
        if (Objects.isNull(eventDTO.getGroup())) {
            errorDTO.setMessage(ErrorMessage.GROUP_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(errorDTO);
        }
        if (Objects.isNull(eventDTO.getEventUserList())) {
            errorDTO.setMessage(ErrorMessage.EVENT_USER_LIST_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(errorDTO);
        }
        for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
            if (eventUserDTO.getUser().getUsername() == null) {
                errorDTO.setMessage(ErrorMessage.USERNAME_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            }
            if (eventUserDTO.getType() == null) {
                errorDTO.setMessage(ErrorMessage.MEMBER_TYPE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            } else if ((eventUserDTO.getType() < EventUserType.HOST.getValue()) || (eventUserDTO.getType() >
                    EventUserType.LISTEN.getValue())) {
                errorDTO.setMessage(ErrorMessage.INVALIDATED_MEMBER_TYPE);
                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errors.add(errorDTO);
            }
            if (!validateSeminarUser(eventDTO)) {
                errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                errorDTO.setMessage(ErrorMessage.NOT_FIND_USER);
                errors.add(errorDTO);
            }
        }
        if (eventDTO.getName() == null) {
            errorDTO.setMessage(ErrorMessage.EVENT_NAME_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errors.add(errorDTO);
        }
        if (eventDTO.getBeginDate() == null || eventDTO.getEndDate() == null) {
            if (eventDTO.getBeginDate() == null) {
                errorDTO.setMessage(ErrorMessage.BEGIN_DATE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            }
            if (eventDTO.getEndDate() == null) {
                errorDTO.setMessage(ErrorMessage.END_DATE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            }
        } else if (!eventDTO.getBeginDate().before(eventDTO.getEndDate())) {
            errorDTO.setMessage(ErrorMessage.END_DATE_AFTER_BEGIN_DATE);
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errors.add(errorDTO);
        }
        return errors;
    }

    private Boolean validateSeminarUser(EventDTO<EventSeminarDetail> eventDTO) {
        List<UserDTO> ldapUserList = ldapUserService.getAllUsers();
        List<UserDTO> ldapUserListClone = new ArrayList<>(ldapUserList);
        List<KpiUser> dbUserList = (List<KpiUser>) kpiUserRepo.findAll();

        ldapUserList.removeIf(userDTO -> dbUserList.stream()
                .anyMatch(kpiUser -> kpiUser.getUserName().equals(userDTO.getUsername())));

        ldapUserList.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
            if (!ldapUserListClone.stream().anyMatch(
                    userDTO -> userDTO.getUsername().equals(eventUserDTO.getUser().getUsername()))) {
                return false;
            }
        }
        return true;
    }

    private Boolean validateTeamBuildingUser(EventDTO<EventTeamBuildingDetail> eventDTO) {
        List<UserDTO> ldapUserList = ldapUserService.getAllUsers();
        List<UserDTO> ldapUserListClone = new ArrayList<>(ldapUserList);
        List<KpiUser> dbUserList = (List<KpiUser>) kpiUserRepo.findAll();

        ldapUserList.removeIf(userDTO -> dbUserList.stream()
                .anyMatch(kpiUser -> kpiUser.getUserName().equals(userDTO.getUsername())));

        ldapUserList.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        for (EventUserDTO eventUserDTO : eventDTO.getEventUserList()) {
            if (!ldapUserListClone.stream().anyMatch(
                    userDTO -> userDTO.getUsername().equals(eventUserDTO.getUser().getUsername()))) {
                return false;
            }
        }
        return true;
    }

    private boolean isEmployee(KpiUser kpiUser, List<UserDTO> userDTOs){
        Boolean isEmployee = false;

        for(UserDTO employee : userDTOs){
            if(employee.getUsername().equals(kpiUser.getUserName())){
                isEmployee = true;
                break;
            }
        }
        return isEmployee;
    }
}
