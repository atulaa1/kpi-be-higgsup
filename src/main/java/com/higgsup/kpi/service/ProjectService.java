package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    List<ProjectDTO> getAllProject();

    ProjectDTO updateProject(ProjectDTO projectDTO);

    ProjectDTO deleteProject(ProjectDTO projectDTO);

    ProjectDTO createProject(ProjectDTO projectDTO);
}
