package com.higgsup.kpi.dto;

public class ProjectEvaluationDTO {
    private ProjectDTO project;

    private Integer rating;

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
