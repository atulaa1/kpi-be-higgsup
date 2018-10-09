package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EmployeeEvaluationDTO;

import java.io.IOException;

public interface EvaluationService {
    EmployeeEvaluationDTO createEmployeeEvaluation(EmployeeEvaluationDTO employeeEvaluationDTO) throws IOException;

}
