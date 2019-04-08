package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.EvaluationInfoDTO;
import com.higgsup.kpi.dto.EvaluationDTO;

import java.io.IOException;
import java.util.List;

public interface EvaluationService {
    EvaluationInfoDTO getAllEvaluationInfo();

    EvaluationDTO createEvaluation(EvaluationDTO employeeEvaluationDTO) throws IOException;

    List<EvaluationDTO> getAllEvaluation();
}
