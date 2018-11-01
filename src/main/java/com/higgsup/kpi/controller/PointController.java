package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EmployeeFamePointDetailDTO;
import com.higgsup.kpi.dto.EmployeePointDetailDTO;
import com.higgsup.kpi.dto.PointDTO;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/fame-point")
    public Response getFamePoint(){
        Response<List<EmployeeFamePointDetailDTO>> response = new Response<>(HttpStatus.OK.value());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<EmployeeFamePointDetailDTO> employeeFamePointDetailDTO = pointService.getFamePointOfEmployee(authentication.getPrincipal().toString());
        response.setData(employeeFamePointDetailDTO);
        return response;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/point-detail")
    public Response getPointDetailByUser() {
        Response<List<EmployeePointDetailDTO>> response = new Response<>(HttpStatus.OK.value());
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            List<EmployeePointDetailDTO> employeePointDetailDTOs = pointService.getPointDetailByUser(authentication.getPrincipal().toString());
            response.setData(employeePointDetailDTOs);
        }catch(IOException e){
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        }
        return response;
    }

    @PreAuthorize("hasRole('EMPLOYEE')")
    @GetMapping("/title-board")
    public Response getBestEmployeeOfMonths(){
        Response<List<PointDTO>> response = new Response<>(HttpStatus.OK.value());
        List<PointDTO> bestEmployeeOfMonths = pointService.getBestEmployeeOfMonths();
        response.setData(bestEmployeeOfMonths);
        return response;
    }
}
