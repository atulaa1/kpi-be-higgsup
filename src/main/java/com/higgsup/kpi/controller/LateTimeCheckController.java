package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.LateTimeCheckDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.LateTimeCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/late-times")
public class LateTimeCheckController {
    @Autowired
    LateTimeCheckService lateTimeCheckService;

    @GetMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public Response getAllLateTimeCheckCurrent() {
        Response<List<LateTimeCheckDTO>> response = new Response<>(HttpStatus.OK.value());
        List<LateTimeCheckDTO> lateTimeCheckDTOS = lateTimeCheckService.getAllLateTimeCheckCurrent();
        response.setData(lateTimeCheckDTOS);
        return response;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Response updateLateTimeCheckMonthCurrent(@PathVariable Integer id, @RequestBody(required = true) LateTimeCheckDTO lateTimeCheckDTO) {
        Response response = new Response<>(HttpStatus.OK.value());
        lateTimeCheckDTO.setId(id);
        LateTimeCheckDTO lateTimeCheckDTOS = lateTimeCheckService.updateLateTimeCheckMonthCurrent(lateTimeCheckDTO);
        if (Objects.nonNull(lateTimeCheckDTOS.getErrorCode())) {
            response.setStatus(lateTimeCheckDTOS.getErrorCode());
            response.setMessage(lateTimeCheckDTOS.getMessage());
        } else {
            response.setData(lateTimeCheckDTOS);
        }
        return response;
    }
}
