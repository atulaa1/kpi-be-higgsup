package com.higgsup.kpi.dto;

import java.util.List;

public class EvaluationDTO {
    private List<UserDTO> employeeList;

    private List<ProjectDTO> projectList;

    private UserDTO evaluator;

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

    public UserDTO getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(UserDTO evaluator) {
        this.evaluator = evaluator;
    }
}
