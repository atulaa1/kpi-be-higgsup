package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeeEvaluationDTO extends BaseDTO {

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
}
