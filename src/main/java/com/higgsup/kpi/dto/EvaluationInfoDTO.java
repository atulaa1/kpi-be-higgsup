package com.higgsup.kpi.dto;

import java.util.List;

public class EvaluationInfoDTO {
    private List<UserDTO> employeeList;
    private List<ProjectDTO> projectList;
    private List<SurveyDTO> questionList;

    public List<UserDTO> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<UserDTO> employeeList) {
        this.employeeList = employeeList;
    }

    public List<ProjectDTO> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectDTO> projectList) {
        this.projectList = projectList;
    }

    public List<SurveyDTO> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<SurveyDTO> questionList) {
        this.questionList = questionList;
    }
}
