package com.higgsup.kpi.dto;

public class EmployeeEvaluationDTO {
    private UserDTO evaluator;

    private ProjectEvaluationDTO projectEvaluation;

    private PersonalEvaluationDTO personalEvaluation;

    public UserDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(UserDTO evaluator) {
        this.evaluator = evaluator;
    }

    public ProjectEvaluationDTO getProjectEvaluation() {
        return projectEvaluation;
    }

    public void setProjectEvaluation(ProjectEvaluationDTO projectEvaluation) {
        this.projectEvaluation = projectEvaluation;
    }

    public PersonalEvaluationDTO getPersonalEvaluation() {
        return personalEvaluation;
    }

    public void setPersonalEvaluation(PersonalEvaluationDTO personalEvaluation) {
        this.personalEvaluation = personalEvaluation;
    }
}
