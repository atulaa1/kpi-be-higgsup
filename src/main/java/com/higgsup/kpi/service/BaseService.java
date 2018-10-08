package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSeminarDetail;
import com.higgsup.kpi.dto.EventUserDTO;
import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.entity.KpiEventUser;
import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.glossary.EventUserType;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.glossary.PointValue;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiPointRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.impl.EventServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.higgsup.kpi.glossary.PointValue.MAX_CLUB_POINT;

public abstract class BaseService {

    @Autowired
    KpiPointRepo kpiPointRepo;

    @Autowired
    KpiEventRepo kpiEventRepo;

    @Autowired
    EventServiceImpl kpiEventService;

    protected void addClubPoint(KpiUser kpiUser, Float point) {
        KpiPoint kpiPoint = new KpiPoint();
        kpiPoint.setRatedUser(kpiUser);

        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            kpiPoint.setClubPoint(point);
            kpiPointRepo.save(kpiPoint);
        } else {
            Float currentPoint = kpiPointRepo.findByRatedUser(kpiUser).getClubPoint();
            if (currentPoint < MAX_CLUB_POINT.getValue()) {
                if (MAX_CLUB_POINT.getValue() - currentPoint < point) {
                    kpiPoint.setClubPoint((float) MAX_CLUB_POINT.getValue());
                } else {
                    kpiPoint.setClubPoint(currentPoint + point);
                    kpiPointRepo.save(kpiPoint);
                }
            }
        }
    }

    protected void addSupportPoint(KpiUser kpiUser, Float point) {
        KpiPoint kpiPoint = new KpiPoint();
        if (kpiPointRepo.findByRatedUser(kpiUser) == null) {
            kpiPoint.setSupportPoint(point);
            kpiPoint.setRatedUser(kpiUser);
            kpiPointRepo.save(kpiPoint);
        } else {
            kpiPoint = kpiPointRepo.findByRatedUser(kpiUser);
            kpiPoint.setSupportPoint(point + kpiPoint.getSupportPoint());
            kpiPointRepo.save(kpiPoint);
        }
    }


    protected void addSeminarPoint(List<KpiEventUser> eventUsers, EventDTO<EventSeminarDetail> seminarEventDTO) throws IOException {
        List<KpiEventUser> finishSurveyUsers = eventUsers.stream().filter(e -> e.getStatus() == 1).collect(Collectors.toList());
        for (KpiEventUser eventUser : finishSurveyUsers) {
            if (eventUser.getType().equals(EventUserType.HOST.getValue())) {

            } else if (eventUser.getType().equals(EventUserType.MEMBER.getValue())) {
                KpiUser user = eventUser.getKpiUser();
                if (kpiPointRepo.findByRatedUser(user) == null) {
                    KpiPoint kpiPoint = new KpiPoint();
                    kpiPoint.setRatedUser(user);
                    kpiPoint.setNormalSeminarPoint(Float.parseFloat(seminarEventDTO.getAdditionalConfig().getMemberPoint()));
                    kpiPointRepo.save(kpiPoint);
                } else {
                    KpiPoint kpiPoint = kpiPointRepo.findByRatedUser(user);
                    if(memberPointOfSeminar(eventUser) < PointValue.MAX_NORMAL_SEMINAR_POINT.getValue()){
                        if(PointValue.MAX_NORMAL_SEMINAR_POINT.getValue() - memberPointOfSeminar(eventUser) <
                                Float.parseFloat(seminarEventDTO.getAdditionalConfig().getMemberPoint())){
                            kpiPoint.setNormalSeminarPoint(kpiPoint.getNormalSeminarPoint() + PointValue.MAX_NORMAL_SEMINAR_POINT.getValue() - memberPointOfSeminar(eventUser));
                        }else {
                            kpiPoint.setNormalSeminarPoint(kpiPoint.getNormalSeminarPoint() + memberPointOfSeminar(eventUser));
                        }
                        kpiPointRepo.save(kpiPoint);
                    }
                }
            }
        }
    }

    private float memberPointOfSeminar(KpiEventUser kpiEventUser) throws IOException {
        KpiUser kpiUser = kpiEventUser.getKpiUser();
        List<KpiEvent> seminarEventUserParticipate = kpiEventRepo.findSeminarEventByUser(kpiUser.getUserName());
        List<EventDTO<EventSeminarDetail>> eventDTOS = kpiEventService.convertSeminarEventEntityToDTO(seminarEventUserParticipate);
        Float memberPoint = 0f;

        for (EventDTO<EventSeminarDetail> eventDTO : eventDTOS) {
            Integer index = eventDTO.getEventUserList().indexOf(kpiEventUser);
            List<EventUserDTO> eventUserList = eventDTO.getEventUserList();
            if (eventUserList.get(index).getType().equals(EventUserType.MEMBER.getValue())
                    || eventUserList.get(index).getType().equals(EventUserType.LISTEN.getValue()))
                memberPoint += Float.parseFloat(eventDTO.getAdditionalConfig().getMemberPoint());
        }

        return memberPoint;
    }
}


