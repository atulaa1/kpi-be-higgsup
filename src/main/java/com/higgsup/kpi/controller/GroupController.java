package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping("/groups")

    @PreAuthorize("hasAnyRole('EMPLOYEE','MAN')")
    @PutMapping
    public Response updateTeamBuildingActivity(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException {
        Response response = new Response(HttpStatus.OK.value());
        GroupDTO groupDTO1 = groupService.updateTeamBuildingActivity(groupDTO);
        if(Objects.nonNull(groupDTO1.getErrorCode())){
            response.setStatus(groupDTO1.getErrorCode());
            response.setMessage(groupDTO1.getMessage());
        }
        return response;
    }
}
