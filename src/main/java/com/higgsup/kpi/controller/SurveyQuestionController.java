package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.SurveyQuestionManDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.SurveyQuestionManService;
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
    private SurveyQuestionManService surveyQuestionManService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/questions-man")
    public Response getAll() {
        Response response = new Response(HttpStatus.OK.value());
        List<SurveyQuestionManDTO> surveyQuestionManDTOS = surveyQuestionManService.getAllQuestion();
        response.setData(surveyQuestionManDTOS);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/questions-man")
    public Response updateSurveyQuestionMan(@RequestBody SurveyQuestionManDTO surveyQuestionManDTO){
        Response response = new Response(HttpStatus.OK.value());
        try {
            SurveyQuestionManDTO surveyQuestionManDTO1;
            surveyQuestionManDTO1 = surveyQuestionManService.updateSurveyQuestionMan(surveyQuestionManDTO);
            if (Objects.nonNull(surveyQuestionManDTO1.getErrorCode())) {
                response.setStatus(surveyQuestionManDTO1.getErrorCode());
                response.setMessage(surveyQuestionManDTO1.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }

        return response;
    }
}