package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.GroupTypeDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ResponseStatus;
import com.higgsup.kpi.service.GroupTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class GroupTypeController {
    @Autowired
    GroupTypeService groupTypeService;

    @RequestMapping("/group-types")
    @PreAuthorize("hasRole('ADMIN')")
    public Response getAll() {
        Response response = new Response(ResponseStatus.SUCCESS.getValue());
        List<GroupTypeDTO> groupTypeDTOS = groupTypeService.getAllGroupType();
        response.setData(groupTypeDTOS);
        return response;
    }
}
