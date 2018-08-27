package com.higgsup.kpi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/groups/support")
    public Response createSupport(@RequestBody GroupDTO<GroupSupportDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO group = groupService.createSupport(groupDTO);
            if (Objects.nonNull(group.getErrorCode())) {
                response.setStatus(groupDTO.getErrorCode());
                response.setMessage(groupDTO.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/groups/support")
    public Response updateSupport(@RequestBody GroupDTO<GroupSupportDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO group = groupService.updateSupport(groupDTO);
            if (Objects.nonNull(group.getErrorCode())) {
                response.setStatus(groupDTO.getErrorCode());
                response.setMessage(groupDTO.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @RequestMapping(value = "/groups/seminars", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ADMIN')")
    public Response createSeminar(@RequestBody GroupDTO<GroupSeminarDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO groupDTORP = groupService.createSeminar(groupDTO);

            if (Objects.nonNull(groupDTORP.getErrorCode())) {
                response.setStatus(groupDTORP.getErrorCode());
                response.setMessage(groupDTORP.getMessage());
            }
        } catch (JsonProcessingException e) {
            response.setMessage(ErrorCode.JSON_PROCESSING_EXCEPTION.getDescription());
            response.setStatus(ErrorCode.JSON_PROCESSING_EXCEPTION.getValue());
        }
        return response;
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping("groups/team-building")
    public Response updateTeamBuilding(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO groupDTOResponse = groupService.updateTeamBuilding(groupDTO);

            if (Objects.nonNull(groupDTOResponse.getErrorCode())) {
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
            GroupDTO groupDTO1 = groupService.createClub(groupDTO);

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

    @RequestMapping(value = "groups/seminars/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ADMIN')")
    public Response updateSeminar(@PathVariable Integer id, @RequestBody GroupDTO<GroupSeminarDetail> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            groupDTO.setId(id);
            GroupDTO groupDTO1 = groupService.updateSeminar(groupDTO);

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
    public Response createTeamBuilding(@RequestBody GroupDTO<TeamBuildingDTO> groupDTO) {
        Response response = new Response(HttpStatus.OK.value());
        try {
            GroupDTO groupDTOTeamBuilding = groupService.createTeamBuilding(groupDTO);

            if (Objects.nonNull(groupDTOTeamBuilding.getErrorCode())) {
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
    public Response updateClub(@PathVariable Integer id, @RequestBody GroupDTO<GroupClubDetail> groupDTO) {
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

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/groups")
    public Response getAllGroup() {
        Response<List<GroupDTO>> response = new Response(HttpStatus.OK.value());
        try {
            List<GroupDTO> groupDTOS = groupService.getAllGroup();
            response.setData(groupDTOS);
        } catch (IOException e) {
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }

}
