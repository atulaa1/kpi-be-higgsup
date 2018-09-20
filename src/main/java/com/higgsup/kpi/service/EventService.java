package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EventService {
    List<EventDTO> getAllEvent() throws IOException;

    List<EventDTO> getEventCreatedByUser(String username) throws IOException;

    EventDTO createSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO updateSupportEvent(EventDTO<List<EventSupportDetail>> supportDTO) throws IOException, NoSuchFieldException;

    EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO updateClub(EventDTO<EventClubDetail> eventDTO) throws IOException;

    EventDTO createSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException;

    EventDTO updateSeminar(EventDTO<EventSeminarDetail> eventDTO) throws IOException;

    EventDTO confirmOrCancelEvent(EventDTO eventDTO) throws IOException, NoSuchFieldException, IllegalAccessException;

    SeminarSurveyDTO createSeminarSurvey(SeminarSurveyDTO seminarSurveyDTO);
}
