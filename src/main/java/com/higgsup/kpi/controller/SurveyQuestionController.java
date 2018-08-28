package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.SurveyDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/survey")
public class SurveyQuestionController {

    @Autowired
    private SurveyService surveyService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/questions-man")
    public Response getAll() {
        Response response = new Response(HttpStatus.OK.value());
        List<SurveyDTO> surveyQuestionManDTOS = surveyService.getAllQuestion();
        response.setData(surveyQuestionManDTOS);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/questions-man")
    public Response updateSurveyQuestionOfMan(@RequestBody List<SurveyDTO> surveyQuestionManDTOs) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            SurveyDTO surveyQuestionManBaseDTO = surveyService.updateSurvey(surveyQuestionManDTOs);
            if (Objects.nonNull(surveyQuestionManBaseDTO.getErrorCode())) {
                response.setStatus(surveyQuestionManBaseDTO.getErrorCode());
                response.setMessage(surveyQuestionManBaseDTO.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }

        return response;
    }
}