package com.higgsup.kpi.dto;

import java.sql.Timestamp;

public class ProjectUserDTO {
    private Integer id;
    private ProjectDTO project;
    private UserDTO ratedUsername;
    private Timestamp joinedDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProjectDTO getProject() {
        return project;
    }

    public void setProject(ProjectDTO project) {
        this.project = project;
    }

    public UserDTO getRatedUsername() {
        return ratedUsername;
    }

    public void setRatedUsername(UserDTO ratedUsername) {
        this.ratedUsername = ratedUsername;
    }

    public Timestamp getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Timestamp joinedDate) {
        this.joinedDate = joinedDate;
    }
}
