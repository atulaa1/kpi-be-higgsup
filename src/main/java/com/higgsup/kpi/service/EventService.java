package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSupportDetail;

import java.io.IOException;
import java.util.List;

import com.higgsup.kpi.dto.EventClubDetail;

public interface EventService {
    EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO updateClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException;
}
