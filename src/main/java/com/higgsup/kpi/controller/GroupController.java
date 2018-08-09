package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {
    @Autowired
    GroupService groupService;
    @RequestMapping("/groupsSeminar")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping
    public Response updateGroup(@RequestBody GroupDTO groupDTO){
        Response response = new Response(HttpStatus.OK.value());
        groupService.updateSeminar(groupDTO);
        return response;
    }
}
