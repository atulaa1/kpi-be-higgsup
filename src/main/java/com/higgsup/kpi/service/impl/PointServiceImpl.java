package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.*;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.BaseService;
import com.higgsup.kpi.service.PointService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.higgsup.kpi.glossary.EventUserType.ORGANIZER;
import static com.higgsup.kpi.glossary.PointValue.FULL_RULE_POINT;
import static com.higgsup.kpi.glossary.PointValue.LATE_TIME_POINT;

@Service
public class PointServiceImpl extends BaseService implements PointService {

    @Autowired
    private KpiLateTimeCheckRepo kpiLateTimeCheckRepo;

    @Autowired
    private KpiPointRepo kpiPointRepo;

    @Autowired
    private KpiMonthRepo kpiMonthRepo;

    @Autowired
    private KpiProjectUserRepo kpiProjectUserRepo;

    @Autowired
    EventServiceImpl kpiEventService;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    KpiSeminarSurveyRepo kpiSeminarSurveyRepo;

    @Autowired
    KpiGroupTypeRepo kpiGroupTypeRepo;

    @Autowired
    KpiPointDetailRepo kpiPointDetailRepo;

    @Autowired
    UserService userService;

    @Autowired
    private KpiFamePointRepo kpiFamePointRepo;

    @Scheduled(cron = "00 00 16 10 * ?")
    public void calculateRulePoint() {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByPreviousMonth();

        if (kpiYearMonthOptional.isPresent()) {
            List<KpiLateTimeCheck> LateTimeCheckList = kpiLateTimeCheckRepo.findByMonth(kpiYearMonthOptional.get());

            for (KpiLateTimeCheck kpiLateTimeCheck : LateTimeCheckList) {

                Float rulePoint = (float) (FULL_RULE_POINT.getValue() + kpiLateTimeCheck.getLateTimes() * LATE_TIME_POINT.getValue());

                KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(kpiLateTimeCheck.getUser().getUserName(), kpiYearMonthOptional.get().getId());
                if (Objects.nonNull(kpiPoint)) {
                    kpiPoint.setRulePoint(rulePoint);
                    kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + rulePoint);
                    kpiPointRepo.save(kpiPoint);

                } else {
                    kpiPoint = new KpiPoint();
                    kpiPoint.setRatedUser(kpiLateTimeCheck.getUser());
                    kpiPoint.setRulePoint(rulePoint);
                    kpiPoint.setTotalPoint(rulePoint);
                    kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
                    kpiPointRepo.save(kpiPoint);
                }

                KpiPointDetail kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setUser(kpiLateTimeCheck.getUser());
                kpiPointDetail.setPointType(PointType.RULE_POINT.getValue());
                kpiPointDetail.setPoint(rulePoint);
                kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }

    private void calculateOrganizerPoint(EventDTO<EventTeamBuildingDetail> teamBuildingDTO, List<KpiEventUser> kpiEventUserList,
                                         List<KpiEventUser> participants) {
        Float organizerPoint = teamBuildingDTO.getAdditionalConfig().getOrganizerPoint();
        Float addedPoint = 0F;
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();

        List<KpiEventUser> organizers = kpiEventUserList.stream().filter(u -> u.getType()
                .equals(ORGANIZER.getValue())).collect(Collectors.toList());

        for (KpiEventUser kpiEventUser : organizers) {
            KpiUser kpiUserOrganizer = kpiUserRepo.findByUserName(kpiEventUser.getKpiEventUserPK().getUserName());
            KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(kpiEventUser.getKpiEventUserPK().getUserName(), kpiYearMonthOptional.get().getId());
            if (Objects.nonNull(kpiPoint)) {
                List<KpiEventUser> gamingOrganizers = participants.stream()
                        .filter(u -> kpiUserOrganizer.equals(u.getKpiUser())).collect(Collectors.toList());

                if (gamingOrganizers.isEmpty()) {
                    addedPoint = organizerPoint;
                    kpiPoint.setTeambuildingPoint(kpiPoint.getTeambuildingPoint() + addedPoint);
                    kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + addedPoint);

                } else {
                    for (KpiEventUser gamingOrganizer : gamingOrganizers) {
                        EventUserType eventUserType = EventUserType.getEventUserType(gamingOrganizer.getType());
                        addedPoint = calculateAddedPointForOrganizer(teamBuildingDTO, eventUserType);
                        kpiPoint.setTeambuildingPoint(kpiPoint.getTeambuildingPoint() + addedPoint);
                        kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + addedPoint);
                    }
                }

            } else {
                kpiPoint = new KpiPoint();
                kpiPoint.setRatedUser(kpiUserOrganizer);
                List<KpiEventUser> gamingOrganizers = participants.stream()
                        .filter(u -> kpiUserOrganizer.equals(u.getKpiUser())).collect(Collectors.toList());
                if (gamingOrganizers.isEmpty()) {
                    addedPoint = organizerPoint;
                    kpiPoint.setTeambuildingPoint(addedPoint);
                    kpiPoint.setTotalPoint(addedPoint);
                    kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
                } else {
                    for (KpiEventUser gamingOrganizer : gamingOrganizers) {
                        EventUserType eventUserType = EventUserType.getEventUserType(gamingOrganizer.getType());
                        addedPoint = calculateAddedPointForOrganizer(teamBuildingDTO, eventUserType);
                        kpiPoint.setTeambuildingPoint(addedPoint);
                        kpiPoint.setTotalPoint(addedPoint);
                        kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
                    }
                }
            }
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(convertEventDTOToEntity(teamBuildingDTO));
            kpiPointDetail.setUser(kpiUserRepo.findByUserName(kpiEventUser.getKpiEventUserPK().getUserName()));
            kpiPointDetail.setPointType(PointType.TEAMBUILDING_POINT.getValue());
            kpiPointDetail.setPoint(addedPoint);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);
        }
    }

    @Override
    public void calculateTeambuildingPoint(EventDTO<EventTeamBuildingDetail> teamBuildingDTO) {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        List<UserDTO> employee = userService.getAllEmployee();
        Float addedPoint;

        List<KpiEventUser> kpiEventUserList = kpiEventUserRepo.findByKpiEventId(teamBuildingDTO.getId());

        List<KpiEventUser> eventUserWithoutMan = kpiEventUserList.stream()
                .filter(u -> isEmployee(u.getKpiEventUserPK().getUserName(), employee))
                .collect(Collectors.toList());

        List<KpiEventUser> participants = eventUserWithoutMan.stream()
                .filter(u -> !u.getType().equals(ORGANIZER.getValue()))
                .collect(Collectors.toList());

        for (KpiEventUser participant : participants) {
            KpiUser userParticipant = kpiUserRepo.findByUserName(participant.getKpiEventUserPK().getUserName());
            KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(userParticipant.getUserName(), kpiYearMonthOptional.get().getId());
            if (Objects.nonNull(kpiPoint)) {
                EventUserType eventUserType = EventUserType.getEventUserType(participant.getType());
                addedPoint = calculatePointForParticipants(teamBuildingDTO, eventUserType);
                kpiPoint.setTeambuildingPoint(kpiPoint.getTeambuildingPoint() + addedPoint);
                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + addedPoint);
            } else {
                kpiPoint = new KpiPoint();
                kpiPoint.setRatedUser(userParticipant);
                EventUserType eventUserType = EventUserType.getEventUserType(participant.getType());
                addedPoint = calculatePointForParticipants(teamBuildingDTO, eventUserType);
                kpiPoint.setTeambuildingPoint(addedPoint);
                kpiPoint.setTotalPoint(addedPoint);
                kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
            }
            kpiPointRepo.save(kpiPoint);

            KpiPointDetail kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(convertEventDTOToEntity(teamBuildingDTO));
            kpiPointDetail.setUser(kpiUserRepo.findByUserName(participant.getKpiEventUserPK().getUserName()));
            kpiPointDetail.setPointType(PointType.TEAMBUILDING_POINT.getValue());
            kpiPointDetail.setPoint(addedPoint);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
            kpiPointDetailRepo.save(kpiPointDetail);
        }
        calculateOrganizerPoint(teamBuildingDTO, eventUserWithoutMan, participants);
    }

    private Float calculateAddedPointForOrganizer(EventDTO<EventTeamBuildingDetail> teamBuildingDTO, EventUserType eventUserType){
        Float addedPoint = 0F;
        Float firstPrizePoint = teamBuildingDTO.getAdditionalConfig().getFirstPrizePoint();
        Float secondPrizePoint = teamBuildingDTO.getAdditionalConfig().getSecondPrizePoint();
        Float thirdPrizePoint = teamBuildingDTO.getAdditionalConfig().getThirdPrizePoint();
        Float organizerPoint = teamBuildingDTO.getAdditionalConfig().getOrganizerPoint();

        switch (Objects.requireNonNull(eventUserType)) {
            case FIRST_PLACE: {
                if (organizerPoint > firstPrizePoint) {
                    addedPoint = organizerPoint;
                } else {
                    addedPoint = firstPrizePoint;
                }
                break;
            }
            case SECOND_PLACE: {
                if (organizerPoint > secondPrizePoint) {
                    addedPoint = organizerPoint;
                } else {
                    addedPoint = secondPrizePoint;
                }
                break;
            }
            case THIRD_PLACE: {
                if (organizerPoint > thirdPrizePoint) {
                    addedPoint = organizerPoint;
                } else {
                    addedPoint = thirdPrizePoint;
                }
                break;
            }
        }
        return addedPoint;
    }

    private Float calculatePointForParticipants(EventDTO<EventTeamBuildingDetail> teamBuildingDTO, EventUserType eventUserType){
        Float firstPrizePoint = teamBuildingDTO.getAdditionalConfig().getFirstPrizePoint();
        Float secondPrizePoint = teamBuildingDTO.getAdditionalConfig().getSecondPrizePoint();
        Float thirdPrizePoint = teamBuildingDTO.getAdditionalConfig().getThirdPrizePoint();
        Float addedPoint = 0F;

        switch (Objects.requireNonNull(eventUserType)) {
            case FIRST_PLACE:
                addedPoint = firstPrizePoint;
                break;
            case SECOND_PLACE:
                addedPoint = secondPrizePoint;
                break;
            case THIRD_PLACE:
                addedPoint = thirdPrizePoint;
                break;
        }
        return addedPoint;
    }

    @Scheduled(cron = "10 00 16 10 * ?")
    private void addEffectivePointForHost() throws IOException {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByPreviousMonth();
        if(kpiYearMonthOptional.isPresent()){
            Integer previousMonth = LocalDate.now().getMonthValue() - 1;
            List<KpiGroup> allClub = kpiGroupRepo.findAllClub();
            List<GroupDTO<GroupClubDetail>> allClubDTO = convertClubGroupEntityToDTO(allClub);
            List<UserDTO> employee = userService.getAllEmployee();

            for(GroupDTO<GroupClubDetail> clubDTO:allClubDTO) {
                KpiUser clubOwner = kpiUserRepo.findByUserName(clubDTO.getAdditionalConfig().getHost());
                if(isEmployee(clubOwner.getUserName(), employee)){
                    List<KpiEvent> confirmClubEvents = kpiEventRepo.findConfirmedClubEventInMonth(clubDTO.getId(), previousMonth);
                    if(confirmClubEvents.size() >= (float)clubDTO.getAdditionalConfig().getMinNumberOfSessions() / 2 ){
                        Long hostParticipate = 0L;
                        for(KpiEvent event:confirmClubEvents){
                            List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(event.getId());
                            if(kpiEventUsers.stream().anyMatch(e -> e.getKpiEventUserPK().getUserName().equals(clubDTO.getAdditionalConfig().getHost()))){
                                hostParticipate += 1;
                            }
                        }
                        if(hostParticipate >= (float)confirmClubEvents.size() * 3/4){
                            KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(clubOwner.getUserName(), kpiYearMonthOptional.get().getId());
                            if(kpiPoint != null){
                                kpiPoint.setClubPoint(kpiPoint.getClubPoint() + clubDTO.getAdditionalConfig().getEffectivePoint());
                                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + clubDTO.getAdditionalConfig().getEffectivePoint());
                                kpiPointRepo.save(kpiPoint);
                            }else{
                                kpiPoint = new KpiPoint();
                                kpiPoint.setRatedUser(clubOwner);
                                kpiPoint.setClubPoint(clubDTO.getAdditionalConfig().getEffectivePoint());
                                kpiPoint.setTotalPoint(clubDTO.getAdditionalConfig().getEffectivePoint());
                                kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());
                                kpiPointRepo.save(kpiPoint);
                            }

                            KpiPointDetail kpiPointDetail = new KpiPointDetail();
                            kpiPointDetail.setUser(clubOwner);
                            kpiPointDetail.setPointType(PointType.CLUB_POINT.getValue());
                            kpiPointDetail.setPoint(clubDTO.getAdditionalConfig().getEffectivePoint());
                            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.get().getId());
                            kpiPointDetailRepo.save(kpiPointDetail);
                        }
                    }
                }
            }
        }
    }

    @Scheduled(cron = "20 00 16 10 * ?")
    private void addUnfinishedSurveySeminarPoint() throws IOException{
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByPreviousMonth();
        if(kpiYearMonthOptional.isPresent()){
            Integer previousMonth = Calendar.getInstance().get(Calendar.MONTH);
            List<KpiEvent> unfinishedSurveySeminarEvent = kpiEventRepo.findUnfinishedSurveySeminarEventInMonth(previousMonth);
            if(unfinishedSurveySeminarEvent.size() > 0){
                for(KpiEvent event: unfinishedSurveySeminarEvent){
                    event.setGroup(kpiGroupRepo.findGroupByEventId(event.getId()));
                    KpiGroupType kpiGroupType = kpiGroupTypeRepo.findByGroupId(event.getGroup().getId());
                    event.getGroup().setGroupType(kpiGroupType);
                    List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(event.getId());
                    event.setKpiEventUserList(kpiEventUsers);
                    EventDTO<EventSeminarDetail> seminarEventDTO = kpiEventService.convertSeminarEntityToDTO(event);
                    addSeminarPoint(kpiEventUsers, seminarEventDTO);
                    event.setStatus(StatusEvent.CONFIRMED.getValue());
                    kpiEventRepo.save(event);
                }
            }
        }
    }

    private List<GroupDTO<GroupClubDetail>> convertClubGroupEntityToDTO(List<KpiGroup> allClub) throws IOException {
        List<GroupDTO<GroupClubDetail>> groupDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(allClub)) {
            for (KpiGroup kpiGroup : allClub) {
                ObjectMapper mapper = new ObjectMapper();
                GroupDTO<GroupClubDetail> groupClubDTO = new GroupDTO<>();

                BeanUtils.copyProperties(kpiGroup, groupClubDTO);
                GroupClubDetail groupClubDetail = mapper.readValue(kpiGroup.getAdditionalConfig(), GroupClubDetail.class);

                groupClubDTO.setAdditionalConfig(groupClubDetail);
                groupClubDTO.setGroupType(convertGroupTypeEntityToDTO(kpiGroup.getGroupType()));
                groupDTOS.add(groupClubDTO);
            }
        }
        return groupDTOS;
    }

    private GroupTypeDTO convertGroupTypeEntityToDTO(KpiGroupType kpiGroupType) {
        GroupTypeDTO groupTypeDTO = new GroupTypeDTO();
        BeanUtils.copyProperties(kpiGroupType, groupTypeDTO, "name");
        return groupTypeDTO;
    }

    public void addSeminarPoint(List<KpiEventUser> eventUsers, EventDTO<EventSeminarDetail> seminarEventDTO) throws IOException {
        List<UserDTO> employee = userService.getAllEmployee();
        List<KpiEventUser> addPointUsers = eventUsers.stream()
                .filter(e -> (isEmployee(e.getKpiEventUserPK().getUserName(), employee)) &&
                        (e.getType().equals(EventUserType.HOST.getValue()) || (!e.getType().equals(EventUserType.HOST.getValue()) && e.getStatus().equals(EvaluatingStatus.FINISH.getValue()))))
                .collect(Collectors.toList());
        KpiEvent kpiEvent = convertEventDTOToEntity(seminarEventDTO);
        KpiYearMonth kpiYearMonth;

        if(LocalDate.now().getMonthValue() == seminarEventDTO.getCreatedDate().getMonth() + 1){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent().get();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth().get();
        }

        for (KpiEventUser eventUser : addPointUsers) {
            if (eventUser.getType().equals(EventUserType.HOST.getValue())) {
                calculateHostPoint(eventUser, kpiEvent, seminarEventDTO, kpiYearMonth);
            } else if (eventUser.getType().equals(EventUserType.MEMBER.getValue())) {
                calculateMemberPoint(eventUser, seminarEventDTO, kpiYearMonth);
            } else if (eventUser.getType().equals(EventUserType.LISTEN.getValue())) {
                calculateListenPoint(eventUser, seminarEventDTO, kpiYearMonth);
            }
        }
    }

    private void calculateHostPoint(KpiEventUser eventUser, KpiEvent kpiEvent, EventDTO<EventSeminarDetail>
            seminarEventDTO, KpiYearMonth kpiYearMonthOptional) throws IOException{
        Float actualPointAdd;
        KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(eventUser.getKpiEventUserPK().getUserName(), kpiYearMonthOptional.getId());
        List<KpiSeminarSurvey> surveysEvaluateHost = kpiSeminarSurveyRepo.findByEvaluatedUsernameAndEvent
                (kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()), kpiEvent);

        if (surveysEvaluateHost.size() == 0) {
            actualPointAdd = (float) PointValue.DEFAULT_EFFECTIVE_POINT.getValue();
        } else {
            Integer totalEvaluatePoint = surveysEvaluateHost.stream().mapToInt(KpiSeminarSurvey::getRating).sum();
            actualPointAdd = (float) totalEvaluatePoint / surveysEvaluateHost.size();
        }

        if (kpiPoint == null) {
            kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
            actualPointAdd += Float.parseFloat(seminarEventDTO.getAdditionalConfig().getHostPoint());
            kpiPoint.setTotalPoint(actualPointAdd);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.getId());

        } else {
            Float remainingPoint = remainingAddedPoint(eventUser, seminarEventDTO);
            actualPointAdd += remainingPoint;
            kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + actualPointAdd);
        }

        KpiPointDetail kpiPointDetail = new KpiPointDetail();
        kpiPointDetail.setEvent(convertEventDTOToEntity(seminarEventDTO));
        kpiPointDetail.setUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
        kpiPointDetail.setPoint(actualPointAdd);
        kpiPointDetail.setYearMonthId(kpiYearMonthOptional.getId());

        saveSeminarPoint(kpiPoint, kpiPointDetail, seminarEventDTO, actualPointAdd);
    }

    private void calculateMemberPoint(KpiEventUser eventUser, EventDTO<EventSeminarDetail> seminarEventDTO, KpiYearMonth kpiYearMonthOptional) throws IOException{
        Float actualPointAdd;

        KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(eventUser.getKpiEventUserPK().getUserName(), kpiYearMonthOptional.getId());

        KpiPointDetail kpiPointDetail = null;

        if (kpiPoint == null) {
            kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
            actualPointAdd = Float.parseFloat(seminarEventDTO.getAdditionalConfig().getMemberPoint());
            kpiPoint.setTotalPoint(actualPointAdd);
            kpiPoint.setYearMonthId(kpiYearMonthOptional.getId());

            kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(convertEventDTOToEntity(seminarEventDTO));
            kpiPointDetail.setUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
            kpiPointDetail.setPoint(actualPointAdd);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.getId());

        } else {
            actualPointAdd = remainingAddedPoint(eventUser, seminarEventDTO);
            if(actualPointAdd > 0){
                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + actualPointAdd);

                kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(convertEventDTOToEntity(seminarEventDTO));
                kpiPointDetail.setUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
                kpiPointDetail.setPoint(actualPointAdd);
                kpiPointDetail.setYearMonthId(kpiYearMonthOptional.getId());
            }
        }

        saveSeminarPoint(kpiPoint, kpiPointDetail, seminarEventDTO, actualPointAdd);
    }

    private void calculateListenPoint(KpiEventUser eventUser, EventDTO<EventSeminarDetail>
            seminarEventDTO, KpiYearMonth kpiYearMonthOptional) throws IOException{

        Float actualPointAdd;
        KpiPointDetail kpiPointDetail = null;
        KpiPoint kpiPoint = kpiPointRepo.findByRatedUsernameAndMonth(eventUser.getKpiEventUserPK().getUserName(), kpiYearMonthOptional.getId());

        if (kpiPoint == null) {
            kpiPoint = new KpiPoint();
            kpiPoint.setRatedUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
            actualPointAdd = Float.parseFloat(seminarEventDTO.getAdditionalConfig().getListenerPoint());
            kpiPoint.setTotalPoint(Float.parseFloat(seminarEventDTO.getAdditionalConfig().getListenerPoint()));
            kpiPoint.setYearMonthId(kpiYearMonthOptional.getId());

            kpiPointDetail = new KpiPointDetail();
            kpiPointDetail.setEvent(convertEventDTOToEntity(seminarEventDTO));
            kpiPointDetail.setUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
            kpiPointDetail.setPoint(actualPointAdd);
            kpiPointDetail.setYearMonthId(kpiYearMonthOptional.getId());

        } else {
            actualPointAdd = remainingAddedPoint(eventUser, seminarEventDTO);
            if(actualPointAdd > 0){
                kpiPoint.setTotalPoint(kpiPoint.getTotalPoint() + actualPointAdd);

                kpiPointDetail = new KpiPointDetail();
                kpiPointDetail.setEvent(convertEventDTOToEntity(seminarEventDTO));
                kpiPointDetail.setUser(kpiUserRepo.findByUserName(eventUser.getKpiEventUserPK().getUserName()));
                kpiPointDetail.setPoint(actualPointAdd);
                kpiPointDetail.setYearMonthId(kpiYearMonthOptional.getId());
            }
        }

        saveSeminarPoint(kpiPoint, kpiPointDetail, seminarEventDTO, actualPointAdd);
    }

    private void saveSeminarPoint(KpiPoint kpiPoint, KpiPointDetail kpiPointDetail, EventDTO<EventSeminarDetail> seminarEventDTO,
                                  Float actualPointAdd){
        if(seminarEventDTO.getCreatedDate().getDay() == 6){
            kpiPoint.setWeekendSeminarPoint(kpiPoint.getWeekendSeminarPoint() + actualPointAdd);
            kpiPointRepo.save(kpiPoint);

            if(kpiPointDetail != null){
                kpiPointDetail.setPointType(PointType.SATURDAY_SEMINAR_POINT.getValue());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }else{
            kpiPoint.setNormalSeminarPoint(kpiPoint.getNormalSeminarPoint() + actualPointAdd);
            kpiPointRepo.save(kpiPoint);

            if(kpiPointDetail != null){
                kpiPointDetail.setPointType(PointType.SATURDAY_SEMINAR_POINT.getValue());
                kpiPointDetailRepo.save(kpiPointDetail);
            }
        }
    }

    private float memberPointOfNormalSeminar(KpiEventUser kpiEventUser, Integer month) throws IOException {
        List<KpiEvent> seminarEventUserParticipateAsMember = kpiEventRepo.findFinishedSurveySeminarEventByUserAsMember
                (kpiEventUser.getKpiEventUserPK().getUserName(), month);
        List<KpiEvent> seminarEventUserParticipateAsListener = kpiEventRepo.findFinishedSurveySeminarEventByUserAsListener
                (kpiEventUser.getKpiEventUserPK().getUserName(), month);
        List<KpiEvent> seminarEventUserParticipateAsHost = kpiEventRepo.findFinishedSurveySeminarEventByUserAsHost
                (kpiEventUser.getKpiEventUserPK().getUserName(), month);
        Float participatePoint = 0f;

        List<EventDTO<EventSeminarDetail>> memberEvents = convertToDTO(seminarEventUserParticipateAsMember);
        List<EventDTO<EventSeminarDetail>> listenerEvents = convertToDTO(seminarEventUserParticipateAsListener);
        List<EventDTO<EventSeminarDetail>> hostEvents = convertToDTO(seminarEventUserParticipateAsHost);

        for (EventDTO<EventSeminarDetail> eventDTO : memberEvents) {
            participatePoint += Float.parseFloat(eventDTO.getAdditionalConfig().getMemberPoint());
        }
        for(EventDTO<EventSeminarDetail> eventDTO : listenerEvents){
            participatePoint += Float.parseFloat(eventDTO.getAdditionalConfig().getListenerPoint());
        }
        for(EventDTO<EventSeminarDetail> eventDTO : hostEvents){
            participatePoint += Float.parseFloat(eventDTO.getAdditionalConfig().getHostPoint());
        }
        return participatePoint;
    }

    private float memberPointOfSaturdaySeminar(KpiEventUser kpiEventUser, Integer month) throws IOException {
        Float memberPoint = 0f;
        List<KpiEvent> seminarEventUserParticipateAsHost = kpiEventRepo.findFinishedSurveySeminarEventByUserAsHostAtSaturday(kpiEventUser.getKpiEventUserPK().getUserName(), month);
        List<KpiEvent> seminarEventUserParticipateAsMember = kpiEventRepo.findFinishedSurveySeminarEventByUserAsMemberAtSaturday(kpiEventUser.getKpiEventUserPK().getUserName(), month);

        List<EventDTO<EventSeminarDetail>> hostEvents = convertToDTO(seminarEventUserParticipateAsHost);
        List<EventDTO<EventSeminarDetail>> memberEvents = convertToDTO(seminarEventUserParticipateAsMember);

        for (EventDTO<EventSeminarDetail> eventDTO : hostEvents) {
            memberPoint += Float.parseFloat(eventDTO.getAdditionalConfig().getHostPoint());
        }
        for (EventDTO<EventSeminarDetail> eventDTO : memberEvents) {
            memberPoint += Float.parseFloat(eventDTO.getAdditionalConfig().getMemberPoint());
        }
        return memberPoint;
    }

    private Float remainingAddedPoint(KpiEventUser eventUser, EventDTO<EventSeminarDetail> seminarEventDTO) throws IOException{
        Integer month;
        Float addedPoint = 0F;
        Float remainingPoint;

        if (LocalDate.now().getMonthValue() == seminarEventDTO.getCreatedDate().getMonth() + 1) {
            month = LocalDate.now().getMonthValue();
        } else {
            month = LocalDate.now().getMonthValue() - 1;
        }

        if (seminarEventDTO.getCreatedDate().getDay() == 6) {
            remainingPoint = PointValue.MAX_WEEKEND_SEMINAR_POINT.getValue() - memberPointOfSaturdaySeminar(eventUser, month);
        } else {
            remainingPoint = PointValue.MAX_NORMAL_SEMINAR_POINT.getValue() - memberPointOfNormalSeminar(eventUser, month);
        }

        if (remainingPoint > 0) {
            addedPoint = remainingPoint;
            if (eventUser.getType().equals(EventUserType.HOST.getValue())) {
                if (remainingPoint >= Float.parseFloat(seminarEventDTO.getAdditionalConfig().getHostPoint())) {
                    addedPoint = Float.parseFloat(seminarEventDTO.getAdditionalConfig().getHostPoint());
                }
            } else if (eventUser.getType().equals(EventUserType.MEMBER.getValue())) {
                if (remainingPoint >= Float.parseFloat(seminarEventDTO.getAdditionalConfig().getMemberPoint())) {
                    addedPoint = Float.parseFloat(seminarEventDTO.getAdditionalConfig().getMemberPoint());
                }
            } else if (eventUser.getType().equals(EventUserType.LISTEN.getValue())) {
                if (remainingPoint >= Float.parseFloat(seminarEventDTO.getAdditionalConfig().getListenerPoint())) {
                    addedPoint = Float.parseFloat(seminarEventDTO.getAdditionalConfig().getListenerPoint());
                }
            }
        }
        return addedPoint;
    }

    private KpiEvent convertEventDTOToEntity(EventDTO eventDTO){
        KpiEvent kpiEvent = new KpiEvent();
        BeanUtils.copyProperties(eventDTO, kpiEvent);
        return kpiEvent;
    }

    private List<EventDTO<EventSeminarDetail>> convertToDTO(List<KpiEvent> kpiEvents) throws IOException{
        for(KpiEvent event: kpiEvents) {
            event.setGroup(kpiGroupRepo.findGroupByEventId(event.getId()));
            KpiGroupType kpiGroupType = kpiGroupTypeRepo.findByGroupId(event.getGroup().getId());
            event.getGroup().setGroupType(kpiGroupType);
            List<KpiEventUser> kpiEventUsers = kpiEventUserRepo.findByKpiEventId(event.getId());
            event.setKpiEventUserList(kpiEventUsers);
        }
        return kpiEventService.convertSeminarEventEntityToDTO(kpiEvents);
    }

    @Override
    public List<EmployeePointDetailDTO> getPointDetailByUser(String username) {
        List<EmployeePointDetailDTO> employeePointDetailDTOs = new ArrayList<>();
        List<KpiPoint> listPoint = kpiPointRepo.findByRatedUsername(username);
        if (listPoint != null) {
            for (KpiPoint kpiPoint : listPoint) {
                EmployeePointDetailDTO employeePointDetailDTO = new EmployeePointDetailDTO();
                List<KpiPointDetail> kpiPointDetails = kpiPointDetailRepo.findByUsernameAndYearMonthId(username, kpiPoint.getYearMonthId());
                List<PointDetailDTO> pointDetailDTOs = new ArrayList<>();
                if (kpiPointDetails != null) {
                    for (KpiPointDetail kpiPointDetail : kpiPointDetails) {
                        PointDetailDTO pointDetailDTO = convertPointDetailEntityToDTO(kpiPointDetail);
                        pointDetailDTOs.add(pointDetailDTO);
                    }
                }
                PointDTO pointDTO = convertPointEntityToDTO(kpiPoint);

                KpiYearMonth kpiYearMonth = kpiMonthRepo.findById(kpiPoint.getYearMonthId()).get();
                YearMonthDTO yearMonthDTO = convertYearMonthEntityToDTO(kpiYearMonth);
                employeePointDetailDTO.setPointDetailList(pointDetailDTOs);
                employeePointDetailDTO.setPointTotal(pointDTO);
                employeePointDetailDTO.setYearMonth(yearMonthDTO);
                employeePointDetailDTOs.add(employeePointDetailDTO);
            }
        }
        return employeePointDetailDTOs;
    }

    private EventDTO convertEventEntityToDTO(KpiEvent kpiEvent){
        EventDTO eventDTO = new EventDTO<>();

        eventDTO.setId(kpiEvent.getId());
        eventDTO.setName(kpiEvent.getName());
        eventDTO.setBeginDate(kpiEvent.getBeginDate());
        eventDTO.setEndDate(kpiEvent.getEndDate());

        return eventDTO;
    }

    private PointDTO convertPointEntityToDTO(KpiPoint kpiPoint){
        PointDTO pointDTO = new PointDTO();
        BeanUtils.copyProperties(kpiPoint, pointDTO, "id");
        return pointDTO;
    }

    private PointDetailDTO convertPointDetailEntityToDTO(KpiPointDetail kpiPointDetail){
        PointDetailDTO pointDetailDTO = new PointDetailDTO();
        BeanUtils.copyProperties(kpiPointDetail, pointDetailDTO);
        if (kpiPointDetail.getEvent() != null) {
            EventDTO eventDTO = convertEventEntityToDTO(kpiPointDetail.getEvent());
            pointDetailDTO.setEvent(eventDTO);
        }
        return pointDetailDTO;
    }

    private YearMonthDTO convertYearMonthEntityToDTO(KpiYearMonth kpiYearMonth){
        YearMonthDTO yearMonthDTO = new YearMonthDTO();
        BeanUtils.copyProperties(kpiYearMonth, yearMonthDTO);
        return yearMonthDTO;
    }

    private boolean isEmployee(String username, List<UserDTO> userDTOs){
        Boolean isEmployee = false;

        for(UserDTO employee : userDTOs){
            if(employee.getUsername().equals(username)){
                isEmployee = true;
                break;
            }
        }
        return isEmployee;
    }

    @Scheduled(cron = "05 00 00 01 * ?")
    private void addNewYearMonth(){
        Integer month = LocalDate.now().getMonthValue();
        KpiYearMonth recentYearMonth = kpiMonthRepo.findByMonthCurrent().get();
        KpiYearMonth newYearMonth = new KpiYearMonth();

        if(month == 1){
            newYearMonth.setYearMonth(recentYearMonth.getYearMonth() + 89);
        }else{
            newYearMonth.setYearMonth(recentYearMonth.getYearMonth() + 1);
        }
        kpiMonthRepo.save(newYearMonth);
    }

    @Scheduled(cron = "30 00 16 10 * ?")
    private void setTitleForEmployeeInMonth(){
        int year = Calendar.getInstance().get(Calendar.YEAR);
        Optional<KpiYearMonth> kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        if(kpiYearMonth.isPresent()){
            List<KpiPoint> top3Employee = kpiPointRepo.getTop3EmployeeInMonth(kpiYearMonth.get().getId(), year);
            int size = top3Employee.size();
            if(size > 0){
                top3Employee.get(0).setTitle(Title.BEST_EMPLOYEE_OF_THE_MONTH.getValue());
                kpiPointRepo.save(top3Employee.get(0));
                int index = 1;
                if(index <= size - 1){
                    top3Employee.get(index).setTitle(Title.EMPLOYEE_OF_THE_MONTH_I.getValue());
                    kpiPointRepo.save(top3Employee.get(index));
                    index++;
                    if(index <= size - 1){
                        top3Employee.get(index).setTitle(Title.EMPLOYEE_OF_THE_MONTH_II.getValue());
                        kpiPointRepo.save(top3Employee.get(index));
                    }
                }
            }
        }
    }

    @Scheduled(cron = "40 00 16 10 * ?")
    private void calculateFamePoint(){
        Integer year = LocalDate.now().getYear();
        Optional<KpiYearMonth> kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        if(kpiYearMonth.isPresent()){
            List<KpiPoint> kpiPointList = kpiPointRepo.getAllPointInMonth(kpiYearMonth.get().getId());
            Float famePoint;
            for(KpiPoint kpiPoint: kpiPointList){
                if(kpiPoint.getTitle().equals(Title.BEST_EMPLOYEE_OF_THE_MONTH.getValue())){
                    famePoint = (kpiPoint.getTotalPoint() - 50) / 10 + (float)PointValue.BEST_EMPLOYEE_OF_THE_MONTH_FAME_POINT.getValue();
                }else if(kpiPoint.getTitle().equals(Title.EMPLOYEE_OF_THE_MONTH_I.getValue())){
                    famePoint = (kpiPoint.getTotalPoint() - 50) / 10 + (float)PointValue.EMPLOYEE_OF_THE_MONTH_I_FAME_POINT.getValue();
                }else if(kpiPoint.getTitle().equals(Title.EMPLOYEE_OF_THE_MONTH_II.getValue())){
                    famePoint = (kpiPoint.getTotalPoint() - 50) / 10 + (float)PointValue.EMPLOYEE_OF_THE_MONTH_II_FAME_POINT.getValue();
                }else{
                    famePoint = (kpiPoint.getTotalPoint() - 50) / 10;
                }
                kpiPoint.setFamedPoint(famePoint);
                kpiPointRepo.save(kpiPoint);

                KpiFamePoint kpiFamePoint = kpiFamePointRepo.findByUsernameAndYear(year, kpiPoint.getRatedUser().getUserName());
                if(kpiFamePoint == null){
                    kpiFamePoint = new KpiFamePoint();
                    kpiFamePoint.setFamePoint(famePoint);
                    kpiFamePoint.setYear(year);
                    kpiFamePoint.setUser(kpiPoint.getRatedUser());
                }else{
                    kpiFamePoint.setFamePoint(kpiFamePoint.getFamePoint() + famePoint);
                }
                kpiFamePointRepo.save(kpiFamePoint);
            }
        }
    }

    @Override
    public List<EmployeeFamePointDetailDTO> getFamePointOfEmployee(String username) {
        List<EmployeeFamePointDetailDTO> listEmployeeFamePoint = new ArrayList<>();

        List<KpiFamePoint> listFamePointOfEmployee = kpiFamePointRepo.findByUsername(username);
        for(KpiFamePoint kpiFamePoint : listFamePointOfEmployee){
            EmployeeFamePointDetailDTO employeeFamePointDetailDTO = new EmployeeFamePointDetailDTO();
            List<PointDTO> pointDTOs = new ArrayList<>();
            Integer year = kpiFamePoint.getYear();

            List<KpiPoint> famePointInYear = kpiPointRepo.getFamePointOfEmployeeInYear(username, year * 100, year * 100 + 13);
            for(KpiPoint kpiPoint : famePointInYear){
                PointDTO pointDTO = new PointDTO();
                pointDTO.setFamedPoint(kpiPoint.getFamedPoint());
                KpiYearMonth kpiYearMonth = kpiMonthRepo.findById(kpiPoint.getYearMonthId()).get();
                YearMonthDTO yearMonthDTO = new YearMonthDTO();
                yearMonthDTO.setYearMonth(kpiYearMonth.getYearMonth());
                pointDTO.setYearMonth(yearMonthDTO);
                pointDTOs.add(pointDTO);
            }
            FamePointDTO famePointDTO = convertFamePointEntityToDTO(kpiFamePoint);
            employeeFamePointDetailDTO.setFamePoint(famePointDTO);
            employeeFamePointDetailDTO.setListFamePointInMonth(pointDTOs);
            listEmployeeFamePoint.add(employeeFamePointDetailDTO);
        }
        return listEmployeeFamePoint;
    }

    private FamePointDTO convertFamePointEntityToDTO(KpiFamePoint kpiFamePoint){
        FamePointDTO famePointDTO = new FamePointDTO();
        BeanUtils.copyProperties(kpiFamePoint, famePointDTO, "id");
        return famePointDTO;
    }
}
