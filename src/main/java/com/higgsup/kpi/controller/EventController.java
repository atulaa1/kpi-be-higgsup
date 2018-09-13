package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSeminarDetail;
import com.higgsup.kpi.dto.EventSupportDetail;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/support")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response createSupport(@RequestBody EventDTO<List<EventSupportDetail>> supportDTO) {
        Response response = new Response<>(HttpStatus.OK.value());
        try {
            EventDTO eventDTO = eventService.createSupportEvent(supportDTO);
            if (Objects.nonNull(eventDTO.getErrorCode())) {
                response.setStatus(eventDTO.getErrorCode());
                response.setMessage(eventDTO.getMessage());
                response.setErrors(eventDTO.getErrorDTOS());
            } else {
                response.setData(eventDTO);
            }
        } catch (NoSuchFieldException | IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @PutMapping("/support/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response updateSupport(@PathVariable Integer id, @RequestBody EventDTO<List<EventSupportDetail>> supportDTO) {
        Response response = new Response<>(HttpStatus.OK.value());
        try {
            supportDTO.setId(id);
            EventDTO eventDTO = eventService.updateSupportEvent(supportDTO);
            if (Objects.nonNull(eventDTO.getErrorCode())) {
                response.setStatus(eventDTO.getErrorCode());
                response.setMessage(eventDTO.getMessage());
                response.setErrors(eventDTO.getErrorDTOS());
            } else {
                response.setData(eventDTO);
            }
        } catch (NoSuchFieldException | IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @RequestMapping(value = "/seminar", method = RequestMethod.POST)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response createSeminar(@RequestBody EventDTO<EventSeminarDetail> seminarDetailEventDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            EventDTO eventDTOResponse = eventService.createSeminar(seminarDetailEventDTO);

            if (Objects.nonNull(eventDTOResponse.getErrorCode())) {
                response.setStatus(eventDTOResponse.getErrorCode());
                response.setMessage(eventDTOResponse.getMessage());
                response.setErrors(eventDTOResponse.getErrorDTOS());
            } else {
                response.setData(eventDTOResponse);
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @RequestMapping(value = "/seminar/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response updateSeminar(@PathVariable Integer id,
                                  @RequestBody EventDTO<EventSeminarDetail> seminarDetailEventDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            seminarDetailEventDTO.setId(id);
            EventDTO eventDTOResponse = eventService.updateSeminar(seminarDetailEventDTO);

            if (Objects.nonNull(eventDTOResponse.getErrorCode())) {
                response.setStatus(eventDTOResponse.getErrorCode());
                response.setMessage(eventDTOResponse.getMessage());
                response.setErrors(eventDTOResponse.getErrorDTOS());

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
