package com.higgsup.kpi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.dto.TeamBuildingDTO;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {
    @Autowired
    GroupService groupService;

    @RequestMapping("/groups")

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/clubs")
    public Response createClub(@RequestBody GroupDTO<GroupClubDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO groupDTO1;
            groupDTO1 = groupService.createClub(groupDTO);
            if (Objects.nonNull(groupDTO1.getErrorCode())) {
                response.setStatus(groupDTO1.getErrorCode());
                response.setMessage(groupDTO1.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE','MAN')")
    @PostMapping("/team-building")
    public Response createConfigTeamBuilding(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO) throws JsonProcessingException {
        Response response = new Response(HttpStatus.OK.value());
        GroupDTO groupDTO1 = groupService.createConfigTeamBuilding(groupDTO);
        if(Objects.nonNull(groupDTO1.getErrorCode())){
            response.setStatus(groupDTO1.getErrorCode());
            response.setMessage(groupDTO1.getMessage());
        }
        return response;
    }
}
