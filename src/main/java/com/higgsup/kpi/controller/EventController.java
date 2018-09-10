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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/support")
    public Response createSupport(@RequestBody EventDTO<List<EventSupportDetail>> supportDTO) {
        Response response = new Response<>(HttpStatus.OK.value());
        try {
            EventDTO eventDTO = eventService.createSupportEvent(supportDTO);
            if (Objects.nonNull(eventDTO.getErrorCode())) {
                response.setStatus(eventDTO.getErrorCode());
                response.setMessage(eventDTO.getMessage());
                if (eventDTO.getErrorDTOS().size() > 0) {
                    response.setData(eventDTO.getErrorDTOS());
                }
            } else {
                response.setData(eventDTO);
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        } catch (NoSuchFieldException e) {
            response.setStatus(ErrorCode.ERROR_NO_SUCH_FIELD_EXCEPTION.getValue());
            response.setMessage(ErrorCode.ERROR_NO_SUCH_FIELD_EXCEPTION.getDescription());
        } catch (IOException e) {
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }
}
