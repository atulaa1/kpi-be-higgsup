package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventTeamBuildingDetail;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.EventUserType;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.BaseService;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
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

    /*@Autowired
    private KpiProjectLogRepo kpiProjectLogRepo;*/

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

    //@Scheduled(cron = "0 34 18 8 * ?")
    public void calculateRulePoint() {
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();

        if (kpiYearMonthOptional.isPresent()) {
            List<KpiLateTimeCheck> LateTimeCheckList = kpiLateTimeCheckRepo.findByMonth(kpiYearMonthOptional.get());

            for (KpiLateTimeCheck kpiLateTimeCheck : LateTimeCheckList) {

                Float rulePoint = (float) (FULL_RULE_POINT.getValue() + kpiLateTimeCheck.getLateTimes() * LATE_TIME_POINT.getValue());

                if (Objects.nonNull(kpiPointRepo.findByRatedUser(kpiLateTimeCheck.getUser()))) {
                    KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(kpiLateTimeCheck.getUser());
                    kpiPoint.setRulePoint(rulePoint);
                    kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());

                    kpiPointRepo.save(kpiPoint);
                } else {
                    KpiPoint kpiPoint = new KpiPoint();
                    kpiPoint.setRatedUser(kpiLateTimeCheck.getUser());
                    kpiPoint.setRulePoint(rulePoint);
                    kpiPoint.setYearMonthId(kpiYearMonthOptional.get().getId());

                    kpiPointRepo.save(kpiPoint);
                }
            }
        }

    }

    @Override
    @Scheduled(cron = "0 0 16 10 * ?")
    public void calculateNormalSeminarPoint() {

    }

    @Override
    public void calculateTeambuildingPoint(EventDTO<EventTeamBuildingDetail> teamBuildingDTO) {
        Float firstPrizePoint = teamBuildingDTO.getAdditionalConfig().getFirstPrizePoint();
        Float secondPrizePoint = teamBuildingDTO.getAdditionalConfig().getSecondPrizePoint();
        Float thirdPrizePoint = teamBuildingDTO.getAdditionalConfig().getThirdPrizePoint();
        Float organizerPoint = teamBuildingDTO.getAdditionalConfig().getOrganizerPoint();

        KpiPoint kpiPoint = new KpiPoint();

        List<KpiEventUser> kpiEventUserList = kpiEventUserRepo.findByKpiEventId(teamBuildingDTO.getId());

        List<KpiEventUser> organizers = kpiEventUserList.stream().filter(u -> u.getType()
                .equals(ORGANIZER.getValue())).collect(Collectors.toList());

        List<KpiEventUser> participants = kpiEventUserList.stream().filter(u -> !u.getType()
                .equals(ORGANIZER.getValue())).collect(Collectors.toList());

        for (KpiEventUser participant : participants) {

            if (Objects.nonNull(kpiPointRepo.findByRatedUser(participant.getKpiUser()))) {
                KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(participant.getKpiUser());

                EventUserType eventUserType = EventUserType.getEventUserType(participant.getType());
                switch (Objects.requireNonNull(eventUserType)) {
                    case FIRST_PLACE:
                        kpiPoint.setTeambuildingPoint(firstPrizePoint);
                        break;
                    case SECOND_PLACE:
                        kpiPoint.setTeambuildingPoint(secondPrizePoint);
                        break;
                    case THIRD_PLACE:
                        kpiPoint.setTeambuildingPoint(thirdPrizePoint);
                        break;
                }
                kpiPointRepo.save(kpiPoint);
            } else {
                KpiPoint kpiPoint = new KpiPoint();
                kpiPoint.setRatedUser(participant.getKpiUser());
            }


        }

        for (KpiEventUser kpiEventUser : organizers) {
            kpiPoint.setRatedUser(kpiEventUser.getKpiUser());

            List<KpiEventUser> gamingOrganizers = kpiEventUserList.stream()
                    .filter(u -> u.getKpiUser().equals(kpiEventUser.getKpiUser())).collect(Collectors.toList());

            if (gamingOrganizers.isEmpty()){
                kpiPoint.setTeambuildingPoint(organizerPoint);
            } else {
                for (KpiEventUser gamingOrganizer : gamingOrganizers ){

                    EventUserType eventUserType = EventUserType.getEventUserType(gamingOrganizer.getType());
                    switch (Objects.requireNonNull(eventUserType)) {
                        case FIRST_PLACE: {
                            if (organizerPoint > firstPrizePoint) {
                                kpiPoint.setTeambuildingPoint(organizerPoint);
                            } else {
                                kpiPoint.setTeambuildingPoint(firstPrizePoint);
                            }
                            break;
                        }
                        case SECOND_PLACE: {
                            if (organizerPoint > secondPrizePoint) {
                                kpiPoint.setTeambuildingPoint(organizerPoint);
                            } else {
                                kpiPoint.setTeambuildingPoint(secondPrizePoint);
                            }
                            break;
                            }
                        case THIRD_PLACE: {
                            if (organizerPoint > thirdPrizePoint) {
                                kpiPoint.setTeambuildingPoint(organizerPoint);
                            } else {
                                kpiPoint.setTeambuildingPoint(thirdPrizePoint);
                            }
                            break;
                        }
                    }
                }
            }
            kpiPointRepo.save(kpiPoint);
        }
    }
}
