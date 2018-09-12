package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.repository.KpiEventRepo;
import com.higgsup.kpi.repository.KpiEventUserRepo;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private KpiEventRepo kpiEventRepo;

    @Autowired
    private KpiEventUserRepo kpiEventUserRepo;

    @Override
    public List<EventDTO> getAllEvent(){
        List<KpiEvent> eventList = kpiEventRepo.findAllEvent();
        List<KpiEvent> returnEventList = new ArrayList<>();
        for(KpiEvent kpiEvent : eventList)
        {
            if(kpiEvent.getGroup().getGroupType().getId() == 2 || kpiEvent.getGroup().getGroupType().getId() == 4)
            {
                returnEventList.add(kpiEvent);
            }
        }
        return convertEventEntityToDTO(returnEventList);
    }

    private List<EventDTO> convertEventEntityToDTO(List<KpiEvent> kpiEventEntities){
        List<EventDTO> eventDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiEventEntities)) {
            for (KpiEvent kpiEvent : kpiEventEntities) {
                EventDTO eventDTO = new EventDTO();
                BeanUtils.copyProperties(kpiEvent, eventDTO);
                eventDTOS.add(eventDTO);
            }
        }
        return eventDTOS;
    }
}
