package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.*;

import java.io.IOException;
import java.util.List;

public interface EventService {
    List<EventDTO> getAllClubAndSupportEvent() throws IOException;

    List<EventDTO> getAllClubAndSupportEventNewSupport() throws IOException;

    List<EventDTO> getEventCreatedByUser(String username) throws IOException;

    List<EventDTO> getSeminarEventByUser(String username) throws IOException;

    EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO createSupportEventNew(EventDTO<List<EventSupportDTO>>supportDTO) throws IOException;

    EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO updateSupportEventNew(EventDTO<List<EventSupportDTO>>supportDTO) throws IOException, NoSuchFieldException;

    EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO updateClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO createSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException;

    EventDTO updateSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException;

    EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException;

    EventDTO confirmOrCancelEventSupportNew(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException;
}
