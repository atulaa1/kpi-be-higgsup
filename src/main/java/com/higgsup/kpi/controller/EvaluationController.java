package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EvaluationInfoDTO;
import com.higgsup.kpi.dto.EvaluationDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

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

    @PreAuthorize("hasRole('MAN')")
    @PostMapping("/survey")
    public Response createEmployeeEvaluation(@RequestBody EvaluationDTO evaluationDTO) {
        Response<EvaluationDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            EvaluationDTO evaluationDTOResponse = evaluationService.createEvaluation(evaluationDTO);
            if (Objects.nonNull(evaluationDTOResponse.getErrorCode())) {
                response.setStatus(evaluationDTOResponse.getErrorCode());
                response.setMessage(evaluationDTOResponse.getMessage());
                response.setErrors(evaluationDTOResponse.getErrorDTOS());
            } else {
                response.setData(evaluationDTOResponse);
            }
        } catch (IOException e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }

    @PreAuthorize("hasRole('MAN')")
    @GetMapping("/survey")
    public Response getPersonalEvaluation() {
        Response<List<EvaluationDTO>> response = new Response<>(HttpStatus.OK.value());
        List<EvaluationDTO> evaluationDTOS = evaluationService.getAllEvaluation();
        response.setData(evaluationDTOS);
        return response;
    }
}
