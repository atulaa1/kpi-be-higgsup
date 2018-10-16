package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EvaluationInfoDTO;
import com.higgsup.kpi.dto.EmployeeEvaluationDTO;

import java.io.IOException;

public interface EvaluationService {
    EvaluationInfoDTO getAllEvaluationInfo();

    EmployeeEvaluationDTO createEmployeeEvaluation(EmployeeEvaluationDTO employeeEvaluationDTO) throws IOException;

}
