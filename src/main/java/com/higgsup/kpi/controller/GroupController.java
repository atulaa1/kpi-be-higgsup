package com.higgsup.kpi.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
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

    @RequestMapping("/clubs")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PutMapping
    public Response updateClubActivity(@RequestBody GroupDTO groupDTO) throws JsonProcessingException {
        Response response = new Response(HttpStatus.OK.value());
        GroupDTO groupDTO1 = groupService.updateClub(groupDTO);
        response.setData(groupDTO1);
        return response;
    }
}
