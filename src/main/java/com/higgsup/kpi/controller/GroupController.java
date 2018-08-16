package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.GroupSupportDetail;
import com.higgsup.kpi.dto.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupClubDetail;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController{

    @Autowired
    GroupService groupService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/group-support")
    public Response createSupport(@RequestBody GroupDTO<GroupSupportDetail> groupDTO) throws JsonProcessingException
    {
        Response response = new Response(HttpStatus.OK.value());
        GroupDTO group = groupService.createSupport(groupDTO);
        if(Objects.nonNull(group.getErrorCode()))
        {
            response.setStatus(groupDTO.getErrorCode());
            response.setMessage(groupDTO.getMessage());
        }
        return response;
    }
public class GroupController {

    @Autowired
    GroupService groupService;

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

}
