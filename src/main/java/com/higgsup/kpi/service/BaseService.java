package com.higgsup.kpi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSeminarDetail;
import com.higgsup.kpi.dto.EventUserDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.EventUserType;
import com.higgsup.kpi.glossary.GroupType;
import com.higgsup.kpi.glossary.PointValue;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiPointRepo;
import com.higgsup.kpi.repository.KpiSeminarSurveyRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.impl.EventServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
}


