package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EmployeePointDetailDTO;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/point-detail")
    public Response getPointDetailByUser() {
        Response<EmployeePointDetailDTO> response = new Response<>(HttpStatus.OK.value());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            EmployeePointDetailDTO employeePointDetailDTO = pointService.getPointDetailByUser(authentication.getPrincipal().toString());
            response.setData(employeePointDetailDTO);
        }catch(IOException e){
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }
}
