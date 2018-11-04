package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EvaluationInfoDTO;
import com.higgsup.kpi.dto.EmployeeEvaluationDTO;

import java.io.IOException;
import java.util.List;

public interface EvaluationService {
    EvaluationInfoDTO getAllEvaluationInfo();

    EmployeeEvaluationDTO createEmployeeEvaluation(EmployeeEvaluationDTO employeeEvaluationDTO) throws IOException;

    List<EmployeeEvaluationDTO> getAllPersonalEvaluation() throws IOException;
}
