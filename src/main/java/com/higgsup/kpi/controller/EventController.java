package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EventClubDetail;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasAnyRole('MAN','EMPLOYEE')")
    @PostMapping("/club")
    public Response createClub(@RequestBody EventDTO<EventClubDetail> eventDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            EventDTO eventDTOResponse = eventService.createClub(eventDTO);
            if (Objects.nonNull(eventDTOResponse.getErrorCode())) {
                response.setStatus(eventDTOResponse.getErrorCode());
                response.setMessage(eventDTOResponse.getMessage());
            } else {
                response.setData(eventDTOResponse);
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }
}
