package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.LateTimeCheckDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.LateTimeCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/late-times")
public class LateTimeCheckController {
    @Autowired
    LateTimeCheckService lateTimeCheckService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Response getALLLateTimeCheckCurrent() {
        Response<List<LateTimeCheckDTO>> response = new Response<>(HttpStatus.OK.value());
        List<LateTimeCheckDTO> lateTimeCheckDTOS = lateTimeCheckService.getALLLateTimeCheckCurrent();
        response.setData(lateTimeCheckDTOS);
        return response;
    }
}
