package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EvaluationDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PreAuthorize("hasRole('MAN')")
    @GetMapping("/info")
    public Response getEvaluationInfo(){
        Response<EvaluationDTO> response = new Response<>(HttpStatus.OK.value());
        EvaluationDTO evaluationDTO = evaluationService.getAllEvaluationInfo();
        response.setData(evaluationDTO);
        return response;
    }
}
