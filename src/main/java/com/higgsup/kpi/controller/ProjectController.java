package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
