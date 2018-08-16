package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.SurveyQuestionManDTO;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.SurveyQuestionManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
