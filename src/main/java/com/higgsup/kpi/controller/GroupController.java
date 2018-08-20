package com.higgsup.kpi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.glossary.ErrorCode;
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
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/groups/support")
    public Response createSupport(@RequestBody GroupDTO<GroupSupportDetail> groupDTO)
    {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO group = groupService.createSupport(groupDTO);
            if (Objects.nonNull(group.getErrorCode())) {
                response.setStatus(groupDTO.getErrorCode());
                response.setMessage(groupDTO.getMessage());
            }
        }catch(JsonProcessingException e)
        {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/groups/support")
    public Response updateSupport(@RequestBody GroupDTO<GroupSupportDetail> groupDTO)
    {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO group = groupService.updateSupport(groupDTO);
            if (Objects.nonNull(group.getErrorCode())) {
                response.setStatus(groupDTO.getErrorCode());
                response.setMessage(groupDTO.getMessage());
            }
        }catch(JsonProcessingException e){
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("groups/team-building")
    public Response updateTeamBuildingActivity(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());

        try {
            GroupDTO groupDTOResponse = groupService.updateTeamBuildingActivity(groupDTO);
            if(Objects.nonNull(groupDTOResponse.getErrorCode())){
                response.setStatus(groupDTOResponse.getErrorCode());
                response.setMessage(groupDTOResponse.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }

        return response;
    }

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

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("groups/team-building")
    public Response createConfigTeamBuilding(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO){
        Response response = new Response(HttpStatus.OK.value());

        try {
            GroupDTO groupDTOTeamBuilding = groupService.createConfigTeamBuilding(groupDTO);
            if(Objects.nonNull(groupDTOTeamBuilding.getErrorCode())){
                response.setStatus(groupDTOTeamBuilding.getErrorCode());
                response.setMessage(groupDTOTeamBuilding.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }

        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("groups/clubs/{id}")
    public Response updateClubActivity(@PathVariable Integer id, @RequestBody GroupDTO<GroupClubDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            groupDTO.setId(id);
            GroupDTO groupDTO1 = groupService.updateClub(groupDTO);

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
