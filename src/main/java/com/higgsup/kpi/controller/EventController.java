package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/employee")
    public Response getEventCreatedByUser() {
        Response<List<EventDTO>> response = new Response<>(HttpStatus.OK.value());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<EventDTO> eventDTOS = eventService.getEventCreatedByUser(authentication.getPrincipal().toString());
            response.setData(eventDTOS);
        } catch (IOException e) {
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public Response getAllEvent() {
        Response<List<EventDTO>> response = new Response<>(HttpStatus.OK.value());
        try {
            List<EventDTO> eventDTOS = eventService.getAllEvent();
            response.setData(eventDTOS);
        } catch (IOException ex) {
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }

    @PostMapping("/support")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response createSupport(@RequestBody EventDTO<List<EventSupportDetail>> supportDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
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
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
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

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping("/club")
    public Response createClub(@RequestBody EventDTO<EventClubDetail> eventDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            EventDTO eventDTOResponse = eventService.createClub(eventDTO);
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
        } catch (IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping("club/{id}")
    public Response updateClub(@PathVariable Integer id, @RequestBody EventDTO<EventClubDetail> eventDTO) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            eventDTO.setId(id);
            EventDTO eventDTOResponse = eventService.updateClub(eventDTO);
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
        } catch (IOException e) {
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
        } catch (IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
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
        } catch (IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Response confirmOrCancelEvent(@PathVariable Integer id, @RequestBody EventDTO eventParam) {
        Response<EventDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            if (Objects.nonNull(eventParam.getStatus())) {
                eventParam.setId(id);
                EventDTO eventDTO = eventService.confirmOrCancelEvent(eventParam);
                if (Objects.nonNull(eventDTO.getErrorCode())) {
                    response.setStatus(eventDTO.getErrorCode());
                    response.setMessage(eventDTO.getMessage());
                } else {
                    response.setData(eventDTO);
                }
            } else {
                response.setStatus(ErrorCode.NOT_NULL.getValue());
                response.setMessage(ErrorMessage.EVENT_STATUS_CAN_NOT_NULL);
            }
        } catch (Exception e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @PostMapping("/seminar/survey")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public Response createSeminarSurvey(@RequestBody SeminarSurveyDTO seminarSurveyDTO) {
        Response<SeminarSurveyDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            SeminarSurveyDTO seminarSurveyDTOResponse = eventService.createSeminarSurvey(seminarSurveyDTO);

            if (Objects.nonNull(seminarSurveyDTOResponse.getErrorCode())) {
                response.setStatus(seminarSurveyDTOResponse.getErrorCode());
                response.setMessage(seminarSurveyDTOResponse.getMessage());
                response.setErrors(seminarSurveyDTOResponse.getErrorDTOS());
            } else {
                response.setData(seminarSurveyDTOResponse);
            }
        } catch (Exception e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }
}
