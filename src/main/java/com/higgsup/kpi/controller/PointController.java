package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.PointDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/fame-point")
    public Response getFamePoint(){
        Response<List<PointDTO>> response = new Response<>(HttpStatus.OK.value());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<PointDTO> pointDTOS = pointService.getFamePointOfEmployee(authentication.getPrincipal().toString());
        response.setData(pointDTOS);
        return response;
    }
}
