package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.EventClubDetail;
import com.higgsup.kpi.dto.EventDTO;

public interface EventService {
    EventDTO createClub(EventDTO<EventClubDetail> eventDTO) throws JsonProcessingException;

    EventDTO updateClub(EventDTO<EventClubDetail> eventDTO) throws JsonProcessingException;
}
