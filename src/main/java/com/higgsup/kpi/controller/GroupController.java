package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {
    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/groups")
    public Response getAllGroup() {
        Response response = new Response(HttpStatus.OK.value());
        groupService.getAllGroup();
        return response;
    }
}
