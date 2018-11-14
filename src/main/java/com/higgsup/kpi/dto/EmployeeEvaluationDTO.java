package com.higgsup.kpi.dto;

import java.sql.Timestamp;
import java.util.List;

public class EmployeeEvaluationDTO extends BaseDTO {

    private String surveyName;

    private Timestamp evaluatingTime;

    private List<ProjectEvaluationDTO> projectEvaluations;

    private List<PersonalEvaluationDTO> personalEvaluations;

    public List<ProjectEvaluationDTO> getProjectEvaluations() {
        return projectEvaluations;
    }

    public void setProjectEvaluations(List<ProjectEvaluationDTO> projectEvaluations) {
        this.projectEvaluations = projectEvaluations;
    }

    public List<PersonalEvaluationDTO> getPersonalEvaluations() {
        return personalEvaluations;
    }

    public void setPersonalEvaluations(List<PersonalEvaluationDTO> personalEvaluations) {
        this.personalEvaluations = personalEvaluations;
    }

    public Timestamp getEvaluatingTime() {
        return evaluatingTime;
    }

    public void setEvaluatingTime(Timestamp evaluatingTime) {
        this.evaluatingTime = evaluatingTime;
    }

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
