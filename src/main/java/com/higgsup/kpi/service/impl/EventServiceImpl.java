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
import com.higgsup.kpi.service.EventService;
import com.higgsup.kpi.service.GroupService;
import com.higgsup.kpi.service.LdapUserService;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

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
    public EventDTO createSeminar(EventDTO<EventSeminarDetail> eventDTO) throws JsonProcessingException {
        EventDTO validateSeminarDTO = new EventDTO();
        List<ErrorDTO> validates = validateDataSeminarEvent(eventDTO);

        if (CollectionUtils.isEmpty(validates)) {
            KpiEvent kpiEvent = new KpiEvent();
            ObjectMapper mapper = new ObjectMapper();

            String seminarEventConfig = mapper.writeValueAsString(eventDTO.getAdditionalConfig());
            BeanUtils.copyProperties(eventDTO, kpiEvent);

            kpiEvent.setAdditionalConfig(seminarEventConfig);
            kpiEvent.setCreatedDate(new Timestamp(System.currentTimeMillis()));

            Optional<KpiGroup> kpiGroup = kpiGroupRepo.findById(eventDTO.getGroup().getId());

            if (kpiGroup.isPresent()) {
                kpiEvent.setGroup(kpiGroup.get());
                kpiEvent = kpiEventRepo.save(kpiEvent);

                List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent, eventDTO.getEventUserList());
                kpiEventUserRepo.saveAll(eventUsers);

                BeanUtils.copyProperties(kpiEvent, validateSeminarDTO);
                validateSeminarDTO.setGroup(eventDTO.getGroup());
                validateSeminarDTO.setEventUserList(eventDTO.getEventUserList());
            } else {
                validateSeminarDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                validateSeminarDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        }  else {
            validateSeminarDTO.setErrorCode(validates.get(0).getErrorCode());
            validateSeminarDTO.setMessage(validates.get(0).getMessage());
            validateSeminarDTO.setErrorDTOS(validates);
        }
        return validateSeminarDTO;
    }

    @Override
    public EventDTO updateSeminar(EventDTO<EventSeminarDetail> eventDTO) throws JsonProcessingException {
        EventDTO validateSeminarDTO = new EventDTO();
        Optional<KpiEvent> kpiEventOptional = kpiEventRepo.findById(eventDTO.getId());
        List<ErrorDTO> validates = validateDataSeminarEvent(eventDTO);
        if (kpiEventOptional.isPresent()) {
            if (CollectionUtils.isEmpty(validates)) {
                KpiEvent kpiEvent = kpiEventOptional.get();
                eventDTO.setId(kpiEvent.getId());

                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(eventDTO, kpiEvent);
                String seminarEventConfig = mapper.writeValueAsString(eventDTO.getAdditionalConfig());

                kpiEvent.setAdditionalConfig(seminarEventConfig);
                kpiEvent.setCreatedDate(kpiEvent.getCreatedDate());
                kpiEvent.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
                if (kpiEvent.getUpdatedDate().after(eventDTO.getEndDate())) {
                    validateSeminarDTO.setMessage(ErrorMessage.END_DATE_AFTER_UPDATE_DATE);
                    validateSeminarDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                } else {
                    Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(eventDTO.getGroup().getId());

                    if (kpiGroupOptional.isPresent()) {
                        kpiEvent.setGroup(kpiGroupOptional.get());
                        kpiEvent = kpiEventRepo.save(kpiEvent);

                        List<KpiEventUser> eventUsers = convertEventUsersToEntity(kpiEvent,
                                eventDTO.getEventUserList());
                        kpiEventUserRepo.saveAll(eventUsers);

                        BeanUtils.copyProperties(kpiEvent, validateSeminarDTO);
                        validateSeminarDTO.setGroup(eventDTO.getGroup());
                        validateSeminarDTO.setAdditionalConfig(eventDTO.getAdditionalConfig());
                        validateSeminarDTO.setEventUserList(eventDTO.getEventUserList());
                    } else {
                        validateSeminarDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                        validateSeminarDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                    }
                }
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

    public GroupDTO convertConfigSeminarToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSeminarDetail> seminarDetailGroupDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, seminarDetailGroupDTO);
        GroupSeminarDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                GroupSeminarDetail.class);

        seminarDetailGroupDTO.setAdditionalConfig(groupSeminarDetail);
        seminarDetailGroupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return seminarDetailGroupDTO;
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
            if (eventUserDTO.getUser().getUsername() == "") {
                errorDTO.setMessage(ErrorMessage.USERNAME_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            }
            if (eventUserDTO.getType() == null) {
                errorDTO.setMessage(ErrorMessage.MEMBER_TYPE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errors.add(errorDTO);
            } else if ((eventUserDTO.getType() < 1) || (eventUserDTO.getType() > 3)) {
                errorDTO.setMessage(ErrorMessage.INVALIDATED_MEMBER_TYPE);
                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errors.add(errorDTO);
            }
            if (!validateUser(eventDTO)) {
                errorDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                errorDTO.setMessage(ErrorMessage.NOT_FIND_USER);
                errors.add(errorDTO);
            }
        }
        if (eventDTO.getName() == null || eventDTO.getName().length() == 0) {
            errorDTO.setMessage(ErrorMessage.EVENT_NAME_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errors.add(errorDTO);
        }
        if (eventDTO.getStatus() == null) {
            errorDTO.setMessage(ErrorMessage.STATUS_CAN_NOT_NULL);
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errors.add(errorDTO);
        } else if ((eventDTO.getStatus() < 1) || (eventDTO.getStatus() > 3)) {
            errorDTO.setMessage(ErrorMessage.INVALIDATED_STATUS_OF_EVENT);
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errors.add(errorDTO);
        }
        if (eventDTO.getBeginDate() == null || eventDTO.getEndDate() == null) {
            if (eventDTO.getBeginDate() == null) {
                errorDTO.setMessage(ErrorMessage.BEGIN_DATE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errors.add(errorDTO);
            }
            if (eventDTO.getEndDate() == null) {
                errorDTO.setMessage(ErrorMessage.END_DATE_CAN_NOT_NULL);
                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errors.add(errorDTO);
            }
        } else if(!eventDTO.getBeginDate().before(eventDTO.getEndDate())) {
            errorDTO.setMessage(ErrorMessage.END_DATE_AFTER_BEGIN_DATE);
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errors.add(errorDTO);
        }

        return errors;
    }

    private Boolean validateUser(EventDTO<EventSeminarDetail> eventDTO) {
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
}