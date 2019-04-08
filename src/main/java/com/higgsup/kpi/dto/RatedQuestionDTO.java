package com.higgsup.kpi.dto;

public class RatedQuestionDTO {
    private SurveyDTO surveyDTO;
    private Float point;

    public SurveyDTO getSurveyDTO() {
        return surveyDTO;
    }

    public void setSurveyDTO(SurveyDTO surveyDTO) {
        this.surveyDTO = surveyDTO;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }
}
