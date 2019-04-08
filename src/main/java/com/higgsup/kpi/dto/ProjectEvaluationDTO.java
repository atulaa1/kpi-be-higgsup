package com.higgsup.kpi.dto;

public class ProjectEvaluationDTO {

    private ProjectDTO project;

    private Float point;

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }
}
