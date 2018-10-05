package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.PointValue;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.BaseService;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private KpiProjectLogRepo kpiProjectLogRepo;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

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
    @Scheduled(cron = "0 0 16 10 * ?")
    public void calculateNormalSeminarPoint() {

    }

    @Override
    public void calculateTeambuildingPoint(EventDTO<TeamBuildingDTO> teamBuildingDTO) {
        String firstPrizePoint = teamBuildingDTO.getAdditionalConfig().getFirstPrizePoint();
        String secondPrizePoint = teamBuildingDTO.getAdditionalConfig().getSecondPrizePoint();
        String thirdPrizePoint = teamBuildingDTO.getAdditionalConfig().getThirdPrizePoint();
        String organizerPoint = teamBuildingDTO.getAdditionalConfig().getOrganizerPoint();

        KpiPoint kpiPoint = new KpiPoint();

        List<KpiEventUser> kpiEventUserList = kpiEventUserRepo.findByKpiEventId(teamBuildingDTO.getId());

        /* List<KpiPoint> kpiPointList = kpiPointRepo.findByRatedUser();*/


    }
}
