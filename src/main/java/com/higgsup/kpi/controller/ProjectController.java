package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class ProjectController {
    @Autowired
    private ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/projects")
    public Response getProjectList() {
        Response response = new Response(HttpStatus.OK.value());
        List<ProjectDTO> projectDTOS = projectService.getAllProject();
        response.setData(projectDTOS);
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "projects/{id}", method = RequestMethod.PUT)
    public Response updateProject(@PathVariable Integer id, @RequestBody ProjectDTO projectDTO) {
        Response<ProjectDTO> response = new Response<>(HttpStatus.OK.value());
        projectDTO.setId(id);
        ProjectDTO projectRP = projectService.updateProject(projectDTO);
        if (Objects.nonNull(projectRP.getErrorCode())) {
            response.setStatus(projectRP.getErrorCode());
            response.setMessage(projectRP.getMessage());
        } else {
            response.setData(projectRP);
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "projects", method = RequestMethod.POST)
    public Response createProject(@RequestBody ProjectDTO projectDTO) {
        Response<ProjectDTO> response = new Response<>(HttpStatus.OK.value());

        ProjectDTO projectRP = projectService.createProject(projectDTO);
        if (Objects.nonNull(projectRP.getErrorCode())) {
            response.setStatus(projectRP.getErrorCode());
            response.setMessage(projectRP.getMessage());
        } else {
            response.setData(projectRP);
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "projects/{id}", method = RequestMethod.DELETE)
    public Response deleteProject(@PathVariable Integer id) {
        Response response = new Response(HttpStatus.OK.value());
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(id);
        ProjectDTO projectRP = projectService.deleteProject(projectDTO);
        if (Objects.nonNull(projectRP.getErrorCode())) {
            response.setStatus(projectRP.getErrorCode());
            response.setMessage(projectRP.getMessage());
        }
        return response;
    }
}
