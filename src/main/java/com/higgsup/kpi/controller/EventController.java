package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventSupportDetail;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
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
                if (eventDTO.getErrorDTOS().size() > 0) {
                    response.setErrors(eventDTO.getErrorDTOS());
                }
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
                if (eventDTO.getErrorDTOS().size() > 0) {
                    response.setErrors(eventDTO.getErrorDTOS());
                }
            } else {
                response.setData(eventDTO);
            }
        } catch (NoSuchFieldException | IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }
}
