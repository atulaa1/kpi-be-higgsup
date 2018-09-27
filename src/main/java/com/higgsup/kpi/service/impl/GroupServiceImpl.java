package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiGroup;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.entity.KpiSupport;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.repository.KpiGroupTypeRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.repository.KpiSupportRepo;
import com.higgsup.kpi.service.GroupService;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.higgsup.kpi.util.UtilsValidate.isValidPoint;

@Service
public class GroupServiceImpl implements GroupService {

    @Autowired
    KpiSupportRepo kpiSupportRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiGroupTypeRepo kpiGroupTypeRepo;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private LdapUserService ldapUserService;

    @Autowired
    private UserService userService;

    @Override
    public GroupDTO updateTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException {
        GroupDTO<TeamBuildingDTO> validatedGroupDTO = new GroupDTO<>();

        if (checkGroupTypeIdExisted(groupDTO, validatedGroupDTO)) {
            Integer id = groupDTO.getId();
            Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(id);
            if (!kpiGroupOptional.isPresent()) {
                validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND_ITEM.getValue());
                validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_ITEM);
            } else if (validateTeambuildingInfo(groupDTO, validatedGroupDTO)) {
                KpiGroup kpiGroup = kpiGroupOptional.get();
                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup, "id", "createdDate");
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());

                kpiGroup.setAdditionalConfig(clubJson);
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());
                if (kpiGroupType.isPresent()) {
                    kpiGroupOptional.get().setGroupType(kpiGroupType.get());
                    kpiGroup = kpiGroupRepo.save(kpiGroupOptional.get());

                    BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                    validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                    validatedGroupDTO.setAdditionalConfig((groupDTO.getAdditionalConfig()));
                } else {
                    validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND_GROUP_TYPE.getValue());
                    validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                }
            }
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO createSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupSeminarDetail> validatedGroupDTO = new GroupDTO<>();

        if (kpiGroupRepo.findByName(groupDTO.getName()) != null) {
            validatedGroupDTO.setErrorCode(ErrorCode.ALREADY_CREATED.getValue());
            validatedGroupDTO.setMessage(ErrorCode.ALREADY_CREATED.getDescription());
        } else if (validateSeminar(groupDTO, validatedGroupDTO)) {
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();

            String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
            BeanUtils.copyProperties(groupDTO, kpiGroup);

            kpiGroup.setAdditionalConfig(jsonConfigSeminar);
            Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());

            if (kpiGroupType.isPresent()) {
                kpiGroup.setGroupType(kpiGroupType.get());
                kpiGroup = kpiGroupRepo.save(kpiGroup);

                BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
            } else {
                validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO createClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupClubDetail> validatedGroupDTO = new GroupDTO<>();

        if (kpiGroupRepo.findByName(groupDTO.getName()) == null) {
            KpiGroup kpiGroup = new KpiGroup();
            ObjectMapper mapper = new ObjectMapper();

            if (validateClub(groupDTO, validatedGroupDTO)) {
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                BeanUtils.copyProperties(groupDTO, kpiGroup);

                kpiGroup.setAdditionalConfig(clubJson);
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());

                if (kpiGroupType.isPresent()) {
                    kpiGroup.setGroupType(kpiGroupType.get());
                    kpiGroup = kpiGroupRepo.save(kpiGroup);

                    BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                    validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                    validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
                } else {
                    validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                    validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                }
            }
        } else {
            validatedGroupDTO.setMessage(ErrorMessage.GROUP_ALREADY_EXISTS);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO updateSeminar(GroupDTO<GroupSeminarDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupSeminarDetail> validatedGroupDTO = new GroupDTO<>();

        Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(groupDTO.getId());

        if (!kpiGroupOptional.isPresent()) {
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_SEMINAR);
        } else if (validateSeminar(groupDTO, validatedGroupDTO)) {
            KpiGroup kpiGroup = kpiGroupOptional.get();
            groupDTO.setId(kpiGroup.getId());

            ObjectMapper mapper = new ObjectMapper();
            BeanUtils.copyProperties(groupDTO, kpiGroup, "id", "createdDate");
            String seminarJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());

            kpiGroup.setAdditionalConfig(seminarJson);
            Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());

            if (kpiGroupType.isPresent()) {
                kpiGroup.setGroupType(kpiGroupType.get());
                kpiGroup = kpiGroupRepo.save(kpiGroup);

                BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
            } else {
                validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO updateClub(GroupDTO<GroupClubDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupClubDetail> validatedGroupDTO = new GroupDTO<>();

        Optional<KpiGroup> kpiGroupOptional = kpiGroupRepo.findById(groupDTO.getId());

        if (kpiGroupOptional.isPresent()) {
            if (validateClub(groupDTO, validatedGroupDTO)) {
                KpiGroup kpiGroup = kpiGroupOptional.get();
                groupDTO.setId(kpiGroup.getId());

                ObjectMapper mapper = new ObjectMapper();
                BeanUtils.copyProperties(groupDTO, kpiGroup, "createdDate");
                String clubJson = mapper.writeValueAsString(groupDTO.getAdditionalConfig());

                kpiGroup.setAdditionalConfig(clubJson);
                Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());

                if (kpiGroupType.isPresent()) {
                    kpiGroup.setGroupType(kpiGroupType.get());
                    kpiGroup = kpiGroupRepo.save(kpiGroup);

                    BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                    validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                    validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
                } else {
                    validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                    validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                }
            }
        } else {
            validatedGroupDTO.setMessage(ErrorCode.NOT_FIND.getDescription());
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO createTeamBuilding(GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException {
        GroupDTO<TeamBuildingDTO> validatedGroupDTO = new GroupDTO<>();

        if (checkGroupTypeIdExisted(groupDTO, validatedGroupDTO)) {
            Integer groupTypeId = groupDTO.getGroupType().getId();
            KpiGroup findGroupTypeId = kpiGroupRepo.findGroupTypeId(groupTypeId);

            if (findGroupTypeId == null) {
                if (validateTeambuildingInfo(groupDTO, validatedGroupDTO)) {
                    KpiGroup kpiGroup = new KpiGroup();
                    ObjectMapper mapper = new ObjectMapper();

                    String jsonConfigTeamBuilding = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                    BeanUtils.copyProperties(groupDTO, kpiGroup);

                    kpiGroup.setAdditionalConfig(jsonConfigTeamBuilding);
                    Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());

                    if (kpiGroupType.isPresent()) {
                        kpiGroup.setGroupType(kpiGroupType.get());
                        kpiGroup = kpiGroupRepo.save(kpiGroup);
                        BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);

                        validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                        validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());

                    } else {
                        validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                        validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
                    }
                }
            } else {
                validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
                validatedGroupDTO.setMessage(ErrorMessage.TEAM_BUILDING_HAS_BEEN_EXISTED);
            }
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO createSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupSupportDetail> validatedGroupDTO = new GroupDTO<>();

        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());
        if (kpiGroupType.isPresent()) {
            if (kpiGroupRepo.findByGroupType(kpiGroupType) != null) {
                validatedGroupDTO.setErrorCode(ErrorCode.ALREADY_CREATED.getValue());
                validatedGroupDTO.setMessage(ErrorMessage.ALREADY_CREATED);
            } else {
                if (validateNullInformation(groupDTO)) {
                    if (!validatePoint(groupDTO)) {
                        validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                        validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    } else {
                        KpiGroup kpiGroup = new KpiGroup();
                        ObjectMapper mapper = new ObjectMapper();

                        String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                        BeanUtils.copyProperties(groupDTO, kpiGroup);

                        kpiGroup.setAdditionalConfig(jsonConfigSeminar);
                        kpiGroup.setGroupType(kpiGroupType.get());

                        kpiGroup = kpiGroupRepo.save(kpiGroup);
                        BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                        validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                        validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
                    }
                } else {
                    validatedGroupDTO.setErrorCode(ErrorCode.NOT_FILLING_ALL_INFORMATION.getValue());
                    validatedGroupDTO.setMessage(ErrorMessage.NOT_FILLING_ALL_INFORMATION);
                }
            }
        } else {
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
        }
        return validatedGroupDTO;
    }

    @Override
    public GroupDTO createSupportNew(GroupDTO<List<SupportDTO>> groupDTO) throws JsonProcessingException {
        Optional<KpiGroup> groupOptional = Optional.ofNullable(kpiGroupRepo.findGroupTypeId(GroupType.SUPPORT.getId()));
        if (!groupOptional.isPresent()) {
            List<ErrorDTO> errors = validateCreateSupport(groupDTO);
            if (CollectionUtils.isEmpty(errors)) {
                KpiGroup kpiGroup = new KpiGroup();
                BeanUtils.copyProperties(groupDTO, kpiGroup, "createdDate", "groupType", "additionalConfig");
                kpiGroup.setGroupType(kpiGroupTypeRepo.findById(GroupType.SUPPORT.getId()).get());

                kpiGroup = kpiGroupRepo.save(kpiGroup);

                List<KpiSupport> kpiSupports = convertListSupportDTOTOEntity(groupDTO.getAdditionalConfig(), "id");
                Iterable<KpiSupport> supportEntityCreates = kpiSupportRepo.saveAll(kpiSupports);

                List<SupportDTO> supportDTOS = convertListSupportEntityToDTO((List<KpiSupport>) supportEntityCreates);

                BeanUtils.copyProperties(kpiGroup, groupDTO, "groupType", "additionalConfig");
                groupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                groupDTO.setAdditionalConfig(supportDTOS);
            } else {
                groupDTO.setErrorDTOS(errors);
                groupDTO.setErrorCode(errors.get(0).getErrorCode());
                groupDTO.setMessage(errors.get(0).getMessage());
            }
        } else {
            groupDTO.setErrorCode(ErrorCode.DATA_EXIST.getValue());
            groupDTO.setMessage(ErrorMessage.SUPPORT_ALREADY_EXISTS);
        }
        return groupDTO;
    }

    @Override
    public SupportDTO createTaskSupport(SupportDTO groupDTO) throws JsonProcessingException {
        Optional<KpiGroup> groupOptional = Optional.ofNullable(kpiGroupRepo.findGroupTypeId(GroupType.SUPPORT.getId()));
        if (groupOptional.isPresent()) {
            List<ErrorDTO> errorDTOS = validateTaskSupport(groupDTO);
            if (CollectionUtils.isEmpty(errorDTOS)) {
                if (kpiSupportRepo.findByTaskName(groupDTO.getTaskName()).isPresent()) {
                    ErrorDTO errorDTO = new ErrorDTO();
                    errorDTO.setErrorCode(ErrorCode.DATA_EXIST.getValue());
                    errorDTO.setMessage(ErrorMessage.NAME_TASK_SUPPORT_ALREADY_EXISTS);
                    errorDTOS.add(errorDTO);
                } else {
                    KpiSupport kpiSupport = convertListSupportDTOTOEntity(Lists.newArrayList(groupDTO)).get(0);
                    kpiSupport = kpiSupportRepo.save(kpiSupport);
                    groupDTO = convertListSupportEntityToDTO(Lists.newArrayList(kpiSupport)).get(0);
                }

            } else {
                groupDTO.setErrorCode(errorDTOS.get(0).getErrorCode());
                groupDTO.setMessage(errorDTOS.get(0).getMessage());
                groupDTO.setErrorDTOS(errorDTOS);
            }
        } else {
            groupDTO.setErrorCode(ErrorCode.DATA_DOES_NOT_EXIST.getValue());
            groupDTO.setMessage(ErrorMessage.SUPPORT_NOT_EXISTS);
        }
        return groupDTO;
    }

    @Override
    public SupportDTO updateTaskSupport(SupportDTO supportDTO) throws JsonProcessingException {
        Optional<KpiGroup> groupOptional = Optional.ofNullable(kpiGroupRepo.findGroupTypeId(GroupType.SUPPORT.getId()));
        if (groupOptional.isPresent()) {
            List<ErrorDTO> errorDTOS = validateTaskSupport(supportDTO);
            if (CollectionUtils.isEmpty(errorDTOS)) {
                List<KpiSupport> kpiSupport = (List<KpiSupport>) kpiSupportRepo.findAll();
                Optional<KpiSupport> kpiSupportOptional = kpiSupport.stream().filter(
                        support -> support.getId().equals(supportDTO.getId()))
                        .findFirst();
                if (kpiSupportOptional.isPresent()) {
                    if (kpiSupport.stream().anyMatch(support -> support.getTaskName().equals(supportDTO.getTaskName())
                            && !support.getId().equals(supportDTO.getId()))) {
                        supportDTO.setErrorCode(ErrorCode.DATA_EXIST.getValue());
                        supportDTO.setMessage(ErrorMessage.NAME_TASK_SUPPORT_CAN_NOT_DUPLICATE);
                    } else {
                        KpiSupport kpiSupportSave = kpiSupportOptional.get();
                        BeanUtils.copyProperties(supportDTO, kpiSupportSave, "id", "createdDate");
                        kpiSupportSave = kpiSupportRepo.save(kpiSupportSave);
                        supportDTO.setCreatedDate(kpiSupportSave.getCreatedDate());
                    }
                } else {
                    supportDTO.setErrorCode(ErrorCode.DATA_DOES_NOT_EXIST.getValue());
                    supportDTO.setMessage(ErrorMessage.SUPPORT_NOT_EXISTS);
                }

            } else {
                supportDTO.setErrorCode(errorDTOS.get(0).getErrorCode());
                supportDTO.setMessage(errorDTOS.get(0).getMessage());
                supportDTO.setErrorDTOS(errorDTOS);
            }
        } else {
            supportDTO.setErrorCode(ErrorCode.DATA_DOES_NOT_EXIST.getValue());
            supportDTO.setMessage(ErrorMessage.SUPPORT_NOT_EXISTS);
        }
        return supportDTO;
    }

    private List<ErrorDTO> validateTaskSupport(SupportDTO supportDTO) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        if (Strings.isNullOrEmpty(supportDTO.getTaskName())) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errorDTO.setMessage(ErrorMessage.NAME_TASK_SUPPORT_DOES_NOT_ALLOW_NULL);
            errorDTOS.add(errorDTO);
        }
        if (Objects.isNull(supportDTO.getPoint())) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errorDTO.setMessage(ErrorMessage.POINT_SUPPORT_DOES_NOT_ALLOW_NULL);
            errorDTOS.add(errorDTO);
        }
        return errorDTOS;
    }

    private List<SupportDTO> convertListSupportEntityToDTO(List<KpiSupport> supportEntityCreates,
            String... ignoreProperties) {
        List<SupportDTO> supportDTOS = new ArrayList<>();
        for (KpiSupport kpiSupport : supportEntityCreates) {
            SupportDTO supportDTO = new SupportDTO();
            BeanUtils.copyProperties(kpiSupport, supportDTO, ignoreProperties);
            supportDTOS.add(supportDTO);
        }
        return supportDTOS;
    }

    private List<KpiSupport> convertListSupportDTOTOEntity(List<SupportDTO> supportDTOList, String... ignoreProperties) {
        List<KpiSupport> kpiSupports = new ArrayList<>();
        for (SupportDTO supportDTO : supportDTOList) {
            KpiSupport kpiSupport = new KpiSupport();
            BeanUtils.copyProperties(supportDTO, kpiSupport, ignoreProperties);
            kpiSupports.add(kpiSupport);
        }
        return kpiSupports;
    }

    private List<ErrorDTO> validateCreateSupport(GroupDTO<List<SupportDTO>> groupDTO) {
        List<ErrorDTO> errors = new ArrayList<>();
        if (Strings.isNullOrEmpty(groupDTO.getName())) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            errorDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
            errors.add(errorDTO);
        }
        for (int i = 0; i < groupDTO.getAdditionalConfig().size(); i++) {
            SupportDTO supportDTO = groupDTO.getAdditionalConfig().get(i);
            if (Strings.isNullOrEmpty(supportDTO.getTaskName())) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errorDTO.setMessage(ErrorMessage.NAME_TASK_SUPPORT_DOES_NOT_ALLOW_NULL + " at index " + i);
                errors.add(errorDTO);
            }
            if (Objects.isNull(supportDTO.getPoint())) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                errorDTO.setMessage(ErrorMessage.POINT_SUPPORT_DOES_NOT_ALLOW_NULL + " at index " + i);
                errors.add(errorDTO);
            } else if (supportDTO.getPoint() <= 0) {
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                errorDTO.setMessage(ErrorMessage.POINT_SUPPORT_MUST_BIGGER_THAN_ZERO + " at index " + i);
                errors.add(errorDTO);
            }
        }
        Collection<SupportDTO> supportDTOS = groupDTO.getAdditionalConfig().stream()
                .collect(Collectors.toConcurrentMap(o -> o.getTaskName().toLowerCase(), Function.identity(), (p, q) -> p))
                .values();
        if (supportDTOS.size() != groupDTO.getAdditionalConfig().size()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            errorDTO.setMessage(ErrorMessage.NAME_TASK_SUPPORT_CAN_NOT_DUPLICATE);
            errors.add(errorDTO);
        }
        return errors;
    }

    @Override
    public GroupDTO updateSupport(GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException {
        GroupDTO<GroupSupportDetail> validatedGroupDTO = new GroupDTO<>();

        Optional<KpiGroupType> kpiGroupType = kpiGroupTypeRepo.findById(groupDTO.getGroupType().getId());
        if (kpiGroupType.isPresent()) {
            if (kpiGroupRepo.findByGroupType(kpiGroupType) == null) {
                validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
            } else {
                if (validateNullInformation(groupDTO)) {
                    if (!validatePoint(groupDTO)) {
                        validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
                        validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    } else {
                        KpiGroup kpiGroup = kpiGroupRepo.findByGroupType(kpiGroupType);
                        groupDTO.setId(kpiGroup.getId());
                        ObjectMapper mapper = new ObjectMapper();

                        String jsonConfigSeminar = mapper.writeValueAsString(groupDTO.getAdditionalConfig());
                        BeanUtils.copyProperties(groupDTO, kpiGroup, "createdDate");

                        kpiGroup.setAdditionalConfig(jsonConfigSeminar);
                        kpiGroup.setGroupType(kpiGroupType.get());

                        kpiGroup = kpiGroupRepo.save(kpiGroup);
                        BeanUtils.copyProperties(kpiGroup, validatedGroupDTO);
                        validatedGroupDTO.setGroupType(groupDTO.getGroupType());
                        validatedGroupDTO.setAdditionalConfig(groupDTO.getAdditionalConfig());
                    }
                } else {
                    validatedGroupDTO.setErrorCode(ErrorCode.NOT_FILLING_ALL_INFORMATION.getValue());
                    validatedGroupDTO.setMessage(ErrorMessage.NOT_FILLING_ALL_INFORMATION);
                }
            }
        } else {
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            validatedGroupDTO.setMessage(ErrorMessage.NOT_FIND_GROUP_TYPE);
        }
        return validatedGroupDTO;
    }

    private boolean validateNullInformation(GroupDTO<GroupSupportDetail> groupDTO) {
        GroupSupportDetail groupSupportDetail = groupDTO.getAdditionalConfig();

        return (groupSupportDetail.getSupportConferencePoint() != null && groupSupportDetail.getCleanUpPoint() != null
                && groupSupportDetail.getBuyingStuffPoint() != null && groupSupportDetail
                .getWeeklyCleanUpPoint() != null
                && groupSupportDetail.getTrainingPoint() != null);
    }

    private boolean validatePoint(GroupDTO<GroupSupportDetail> groupDTO) {
        String weeklyCleanUpPoint = String.valueOf(groupDTO.getAdditionalConfig().getWeeklyCleanUpPoint());
        String buyingStuffPoint = String.valueOf(groupDTO.getAdditionalConfig().getBuyingStuffPoint());
        String cleanUpPoint = String.valueOf(groupDTO.getAdditionalConfig().getCleanUpPoint());
        String supportConferencePoint = String.valueOf(groupDTO.getAdditionalConfig().getSupportConferencePoint());
        String trainingPoint = String.valueOf(groupDTO.getAdditionalConfig().getTrainingPoint());

        return (isValidPoint(weeklyCleanUpPoint) && isValidPoint(buyingStuffPoint) && isValidPoint(cleanUpPoint)
                && isValidPoint(supportConferencePoint) && isValidPoint(trainingPoint));
    }

    private Boolean validateTeambuildingInfo(GroupDTO<TeamBuildingDTO> groupDTO, GroupDTO validateGroupDTO) {
        boolean validate = false;

        String organizers = groupDTO.getAdditionalConfig().getOrganizerPoint();
        String firstPrize = groupDTO.getAdditionalConfig().getFirstPrizePoint();
        String secondPrize = groupDTO.getAdditionalConfig().getSecondPrizePoint();
        String thirdPrize = groupDTO.getAdditionalConfig().getThirdPrizePoint();

        Double firstPrizeScore = Double.parseDouble(firstPrize);
        Double secondPrizeScore = Double.parseDouble(secondPrize);
        Double thirdPrizeScore = Double.parseDouble(thirdPrize);

        if (firstPrize.length() == 0) {
            validateGroupDTO.setErrorCode(ErrorCode.TEAM_BUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAM_BUILDING_FIRST_PRIZE_SCORE_CAN_NOT_NULL);
        } else if (secondPrize.length() == 0) {
            validateGroupDTO.setErrorCode(ErrorCode.TEAM_BUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAM_BUILDING_SECOND_PRIZE_SCORE_CAN_NOT_NULL);
        } else if (thirdPrize.length() == 0) {
            validateGroupDTO.setErrorCode(ErrorCode.TEAM_BUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAM_BUILDING_THIRD_PRIZE_SCORE_CAN_NOT_NULL);
        } else if (organizers.length() == 0) {
            validateGroupDTO.setErrorCode(ErrorCode.TEAM_BUILDING_PRIZE_SCORE_CAN_NOT_NULL.getValue());
            validateGroupDTO.setMessage(ErrorMessage.TEAM_BUILDING_ORGANIZERS_SCORE_CAN_NOT_NULL);
        } else if (!isValidPoint(firstPrize)) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_FIRST_PRIZE);
        } else if (!isValidPoint(secondPrize)) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_SECOND_PRIZE);
        } else if (!isValidPoint(thirdPrize)) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_THIRD_PRIZE);
        } else if (!isValidPoint(organizers)) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.INVALIDATED_ORGANIZERS_PRIZE);
        } else if (firstPrizeScore <= secondPrizeScore) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE);
        } else if (secondPrizeScore <= thirdPrizeScore) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE);
        } else {
            validate = true;
        }
        return validate;
    }

    private Boolean checkGroupTypeIdExisted(GroupDTO<TeamBuildingDTO> groupDTO, GroupDTO validateGroupDTO) {
        boolean isExisted = false;
        if (Objects.isNull(groupDTO.getGroupType())) {
            validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
            validateGroupDTO.setMessage(ErrorMessage.GROUP_TYPE_CAN_NOT_NULL);
        } else {
            if (groupDTO.getGroupType().getId() == null) {
                validateGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                validateGroupDTO.setMessage(ErrorMessage.GROUP_TYPE_ID_CAN_NOT_NULL);
            } else {
                isExisted = true;
            }
        }
        return isExisted;
    }

    private Boolean validateClub(GroupDTO<GroupClubDetail> groupDTO, GroupDTO validatedGroupDTO) {
        Boolean validate = false;
        Integer hostNameLength = groupDTO.getAdditionalConfig().getHost().length();

        String minNumberOfSessions = String.valueOf(groupDTO.getAdditionalConfig().getMinNumberOfSessions());
        String participationPoint = String.valueOf(groupDTO.getAdditionalConfig().getParticipationPoint());
        String effectivePoint = String.valueOf(groupDTO.getAdditionalConfig().getEffectivePoint());

        if (groupDTO.getName().length() == 0 || hostNameLength == 0) {
            validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_NAME_IS_NOT_VALID);
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
        } else if (minNumberOfSessions.length() == 0 || minNumberOfSessions.length() > 2) {
            validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_MIN_NUMBER_OF_SESSIONS_IS_NOT_VALID);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (groupDTO.getAdditionalConfig().getMinNumberOfSessions() <= 0) {
            validatedGroupDTO.setMessage(ErrorMessage.MIN_SESSION_MUST_BIGGER_THAN_ZERO);
            validatedGroupDTO.setErrorCode(ErrorCode.MIN_SESSION_MUST_BIGGER_THAN_ZERO.getValue());
        } else if (!isValidPoint(participationPoint) || participationPoint.length() == 0) {
            validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!isValidPoint(effectivePoint) || effectivePoint.length() == 0) {
            validatedGroupDTO.setMessage(ErrorMessage.PARAMETERS_POINT_IS_NOT_VALID);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!validateUserName(groupDTO.getAdditionalConfig().getHost())) {
            validatedGroupDTO.setMessage(ErrorMessage.USER_DOES_NOT_EXIST);
            validatedGroupDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
        } else {
            validate = true;
        }
        return validate;
    }

    private Boolean validateSeminar(GroupDTO<GroupSeminarDetail> groupDTO, GroupDTO validatedGroupDTO) {
        Boolean validate = false;
        Float hostPoint = groupDTO.getAdditionalConfig().getHostPoint();
        Float memberPoint = groupDTO.getAdditionalConfig().getMemberPoint();
        Float listenerPoint = groupDTO.getAdditionalConfig().getListenerPoint();

        if (!isValidPoint(String.valueOf(hostPoint))) {
            validatedGroupDTO.setMessage(ErrorMessage.POINT_HOST_IS_NOT_VALIDATE);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!isValidPoint(String.valueOf(memberPoint))) {
            validatedGroupDTO.setMessage(ErrorMessage.POINT_MEMBER_IS_NOT_VALIDATE);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else if (!isValidPoint(String.valueOf(listenerPoint))) {
            validatedGroupDTO.setMessage(ErrorMessage.POINT_LISTENER_IS_NOT_VALIDATE);
            validatedGroupDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
        } else {
            validate = true;
        }
        return validate;
    }

    private Boolean validateUserName(String username) {
        List<UserDTO> ldapUserList = ldapUserService.getAllUsers();
        List<UserDTO> ldapUserListClone = new ArrayList<>(ldapUserList);
        List<KpiUser> dbUserList = (List<KpiUser>) kpiUserRepo.findAll();

        ldapUserList.removeIf(userDTO -> dbUserList.stream()
                .anyMatch(kpiUser -> kpiUser.getUserName().equals(userDTO.getUsername())));

        ldapUserList.forEach(userDTO -> userService.registerUser(userDTO.getUsername()));

        return ldapUserListClone.stream().anyMatch(
                userDTO -> userDTO.getUsername().equals(username));
    }

    @Override
    public List<GroupDTO> getAllGroup() throws IOException {
        List<KpiGroup> groupList = kpiGroupRepo.findAllGroup();
        return convertGroupsEntityToDTO(groupList);
    }

    private List<GroupDTO> convertGroupsEntityToDTO(List<KpiGroup> groupList) throws IOException {
        List<GroupDTO> groupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(groupList)) {
            for (KpiGroup kpiGroup : groupList) {
                switch (GroupType.getGroupType(kpiGroup.getGroupType().getId())) {
                    case SEMINAR:
                        groupDTOS.add(convertConfigSeminarToDTO(kpiGroup));
                        break;
                    case CLUB:
                        groupDTOS.add(convertConfigClubToDTO(kpiGroup));
                        break;
                    case TEAM_BUILDING:
                        groupDTOS.add(convertConfigTeamBuildingToDTO(kpiGroup));
                        break;
                    case SUPPORT:
                        groupDTOS.add(convertConfigSupportToDTO(kpiGroup));
                        break;
                }
            }
        }
        return groupDTOS;
    }

    private GroupDTO convertConfigSupportToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSupportDetail> groupSupportDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, groupSupportDTO);
        GroupSupportDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                GroupSupportDetail.class);

        groupSupportDTO.setAdditionalConfig(groupSeminarDetail);
        groupSupportDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return groupSupportDTO;
    }

    private GroupDTO convertConfigTeamBuildingToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<TeamBuildingDTO> teamBuildingGroupDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, teamBuildingGroupDTO);
        TeamBuildingDTO groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), TeamBuildingDTO.class);

        teamBuildingGroupDTO.setAdditionalConfig(groupSeminarDetail);
        teamBuildingGroupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return teamBuildingGroupDTO;
    }

    private GroupDTO convertConfigSeminarToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupSeminarDetail> seminarGroupDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, seminarGroupDTO);
        GroupSeminarDetail groupSeminarDetail = mapper.readValue(kpiGroup.getAdditionalConfig(),
                GroupSeminarDetail.class);
        seminarGroupDTO.setAdditionalConfig(groupSeminarDetail);
        seminarGroupDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return seminarGroupDTO;
    }

    private GroupDTO convertConfigClubToDTO(KpiGroup kpiGroup) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        GroupDTO<GroupClubDetail> groupClubDTO = new GroupDTO<>();

        BeanUtils.copyProperties(kpiGroup, groupClubDTO);
        GroupClubDetail groupClubDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), GroupClubDetail.class);

        groupClubDTO.setAdditionalConfig(groupClubDetail);
        groupClubDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
        return groupClubDTO;
    }

    private GroupTypeDTO convertGroupTypeEntityToDTO(KpiGroupType kpiGroupType) {
        GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
        BeanUtils.copyProperties(kpiGroupType, groupTypeDTO);
        return groupTypeDTO;
    }

}
