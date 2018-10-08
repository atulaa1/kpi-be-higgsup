package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.BaseService;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.higgsup.kpi.glossary.EventUserType.ORGANIZER;
import static com.higgsup.kpi.glossary.PointValue.FULL_RULE_POINT;
import static com.higgsup.kpi.glossary.PointValue.LATE_TIME_POINT;
import static com.higgsup.kpi.glossary.PointValue.MAX_CLUB_POINT;

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
    private KpiEventUserRepo kpiEventUserRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Scheduled(cron = "0 0 16 10 * ?")
    public void calculateRulePoint() {
        KpiPoint kpiPoint = new KpiPoint();

        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();

        if (kpiYearMonthOptional.isPresent()) {
            List<KpiLateTimeCheck> LateTimeCheckList = kpiLateTimeCheckRepo.findByMonth(kpiYearMonthOptional.get());

            for (KpiLateTimeCheck kpiLateTimeCheck : LateTimeCheckList) {
                Float rulePoint = (float) (FULL_RULE_POINT.getValue() + kpiLateTimeCheck.getLateTimes() * LATE_TIME_POINT.getValue());

                kpiPoint.setRatedUser(kpiLateTimeCheck.getUser());
                kpiPoint.setRulePoint(rulePoint);

                kpiPointRepo.save(kpiPoint);
            }
        }
    }

    @Override
    public void calculateTeambuildingPoint(EventDTO<TeamBuildingDTO> teamBuildingDTO) {
        String firstPrizePoint = teamBuildingDTO.getAdditionalConfig().getFirstPrizePoint();
        String secondPrizePoint = teamBuildingDTO.getAdditionalConfig().getSecondPrizePoint();
        String thirdPrizePoint = teamBuildingDTO.getAdditionalConfig().getThirdPrizePoint();
        String organizerPoint = teamBuildingDTO.getAdditionalConfig().getOrganizerPoint();

        KpiPoint kpiPoint = new KpiPoint();

        List<KpiEventUser> kpiEventUserList = kpiEventUserRepo.findByKpiEventId(teamBuildingDTO.getId());

        List<KpiEventUser> organizers = kpiEventUserList.stream().filter(u -> u.getType()
                .equals(ORGANIZER.getValue())).collect(Collectors.toList());

        List<KpiEventUser> participants = kpiEventUserList.stream().filter(u -> !u.getType()
                .equals(ORGANIZER.getValue())).collect(Collectors.toList());

        for (KpiEventUser participant : participants) {
            kpiPoint.setRatedUser(participant.getKpiUser());
            switch (participant.getType()){
                case 5: kpiPoint.setTeambuildingPoint(Float.valueOf(firstPrizePoint)); break;
                case 6: kpiPoint.setTeambuildingPoint(Float.valueOf(secondPrizePoint)); break;
                case 7: kpiPoint.setTeambuildingPoint(Float.valueOf(thirdPrizePoint)); break;
            }
            kpiPointRepo.save(kpiPoint);
        }

        for (KpiEventUser kpiEventUser : organizers) {
            kpiPoint.setRatedUser(kpiEventUser.getKpiUser());

            List<KpiEventUser> gamingOrganizers = kpiEventUserList.stream()
                    .filter(u -> u.getKpiUser().equals(kpiEventUser.getKpiUser())).collect(Collectors.toList());

            if (gamingOrganizers.isEmpty()){
                    kpiPoint.setTeambuildingPoint(Float.valueOf(organizerPoint));
            } else {
                for (KpiEventUser gamingOrganizer : gamingOrganizers ){
                    switch (gamingOrganizer.getType()){
                        case 5: {
                            if (Float.valueOf(organizerPoint) > Float.valueOf(firstPrizePoint)) {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(organizerPoint));
                            } else {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(firstPrizePoint));
                            }
                            break;
                        }
                        case 6: {
                            if (Float.valueOf(organizerPoint) > Float.valueOf(secondPrizePoint)) {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(organizerPoint));
                            } else {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(secondPrizePoint));
                            }
                            break;
                            }
                        case 7: {
                            if (Float.valueOf(organizerPoint) > Float.valueOf(thirdPrizePoint)) {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(organizerPoint));
                            } else {
                                kpiPoint.setTeambuildingPoint(Float.valueOf(thirdPrizePoint));
                            }
                            break;
                        }
                    }
                }
            }
            kpiPointRepo.save(kpiPoint);
        }
    }

    @Scheduled(cron = "35 04 18 8 * ?")
    private void addEffectivePointForHost() throws IOException{
        System.out.println("hello");
        List<KpiGroup> allClub = kpiGroupRepo.findAllClub();

        List<GroupDTO<GroupClubDetail>> allClubDTO = convertClubGroupEntityToDTO(allClub);

        for(GroupDTO<GroupClubDetail> clubDTO:allClubDTO) {
            KpiUser clubOwner = kpiUserRepo.findByUserName(clubDTO.getAdditionalConfig().getHost());
            KpiEventUser owner = new KpiEventUser();
            owner.setKpiUser(clubOwner);
            List<KpiEvent> eventsOfClub = kpiEventRepo.findEventCreatedByUser(clubDTO.getAdditionalConfig().getHost());
            Integer confirmEventsOfClub = (int) eventsOfClub.stream()
                    .filter(e -> e.getStatus() == 2).count();
            Integer eventsClubOwnerParticipate = (int) eventsOfClub.stream()
                    .filter(e -> e.getKpiEventUserList().contains(owner) && e.getStatus() == 2).count();
            if(confirmEventsOfClub >= clubDTO.getAdditionalConfig().getMinNumberOfSessions() / 2 &&
                    eventsClubOwnerParticipate >= confirmEventsOfClub * 3/4){
                if(kpiPointRepo.findByRatedUser(clubOwner) != null){
                    KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(clubOwner);
                    kpiPoint.setClubPoint(kpiPoint.getClubPoint() + clubDTO.getAdditionalConfig().getEffectivePoint());
                    kpiPointRepo.save(kpiPoint);
                }else{
                    KpiPoint kpiPoint = new KpiPoint();
                    kpiPoint.setRatedUser(clubOwner);
                    kpiPoint.setClubPoint(clubDTO.getAdditionalConfig().getEffectivePoint());
                    kpiPointRepo.save(kpiPoint);
                }
            }
        }
    }

    @Override
    @Scheduled(cron = "0 0 16 10 * ?")
    public void calculateSeminarPoint() {

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
}
