package com.higgsup.kpi.controller;


import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GenericResponseDTO;
import com.higgsup.kpi.dto.GroupDTO;
import com.higgsup.kpi.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupController {

    @Autowired
    GroupService groupService;

    @RequestMapping("/groups")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping
    public ResponseEntity<GenericResponseDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        groupService.createClub(groupDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(GenericResponseDTO.created());
    }
}
