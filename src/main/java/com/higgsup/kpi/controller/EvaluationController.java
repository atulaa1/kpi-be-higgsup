package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EvaluationInfoDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/evaluation")
public class EvaluationController {

    @Autowired
    private EvaluationService evaluationService;

    @PreAuthorize("hasRole('MAN')")
    @GetMapping("/info")
    public Response getEvaluationInfo(){
        Response<EvaluationInfoDTO> response = new Response<>(HttpStatus.OK.value());
        EvaluationInfoDTO evaluationDTO = evaluationService.getAllEvaluationInfo();
        response.setData(evaluationDTO);
        return response;
    }
}
