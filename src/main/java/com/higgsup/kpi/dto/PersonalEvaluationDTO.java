package com.higgsup.kpi.dto;

import java.util.List;

public class PersonalEvaluationDTO {
    private SurveyDTO surveyDTO;

    private List<PointEvaluationDTO> pointEvaluations;

    public SurveyDTO getSurveyDTO() {
        return surveyDTO;
    }

    public void setSurveyDTO(SurveyDTO surveyDTO) {
        this.surveyDTO = surveyDTO;
    }

    public List<PointEvaluationDTO> getPointEvaluations() {
        return pointEvaluations;
    }

    public void setPointEvaluations(List<PointEvaluationDTO> pointEvaluations) {
        this.pointEvaluations = pointEvaluations;
    }
}
