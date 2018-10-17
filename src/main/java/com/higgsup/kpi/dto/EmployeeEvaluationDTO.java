package com.higgsup.kpi.dto;

import java.sql.Timestamp;
import java.util.List;

public class EmployeeEvaluationDTO extends BaseDTO {

    private String surveyName;

    private Timestamp evaluatingTime;

    private UserDTO evaluator;

    private List<ProjectEvaluationDTO> projectEvaluations;

    private List<PersonalEvaluationDTO> personalEvaluationDTOList;

    public UserDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(UserDTO evaluator) {
        this.evaluator = evaluator;
    }

    public List<ProjectEvaluationDTO> getProjectEvaluations() {
        return projectEvaluations;
    }

    public void setProjectEvaluations(List<ProjectEvaluationDTO> projectEvaluations) {
        this.projectEvaluations = projectEvaluations;
    }

    public List<PersonalEvaluationDTO> getPersonalEvaluationDTOList() {
        return personalEvaluationDTOList;
    }

    public void setPersonalEvaluationDTOList(List<PersonalEvaluationDTO> personalEvaluationDTOList) {
        this.personalEvaluationDTOList = personalEvaluationDTOList;
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
