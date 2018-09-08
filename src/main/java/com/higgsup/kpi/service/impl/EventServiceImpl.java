package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiGroupRepo;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    private KpiGroupRepo kpiGroupRepo;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;
}
