package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSupportDetail;

import java.io.IOException;
import java.util.List;

public interface EventService {
    EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException;
}
