package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.LateTimeCheckDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.service.LateTimeCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public Response updateLateTimesOfCurrentMonth(@PathVariable Integer id,
                                                  @RequestBody(required = true) LateTimeCheckDTO lateTimeCheckDTO) {
        Response response = new Response<>(HttpStatus.OK.value());
        lateTimeCheckDTO.setId(id);
        LateTimeCheckDTO lateTimeCheckDTOS = lateTimeCheckService.updateLateTimesOfCurrentMonth(lateTimeCheckDTO);
        if (Objects.nonNull(lateTimeCheckDTOS.getErrorCode())) {
            response.setStatus(lateTimeCheckDTOS.getErrorCode());
            response.setMessage(lateTimeCheckDTOS.getMessage());
        } else {
            response.setData(lateTimeCheckDTOS);
        }
        return response;
    }

    @PostMapping("/import-file")
    @PreAuthorize("hasRole('ADMIN')")
    public Response processExcelFile(@RequestParam("file") MultipartFile file) {
        Response<List<LateTimeCheckDTO>> response = new Response<>(HttpStatus.OK.value());
        try {
            List<LateTimeCheckDTO> lateTimeCheckDTOS = lateTimeCheckService.processExcelFile(file);
            if (Objects.nonNull(lateTimeCheckDTOS.get(0).getErrorDTOS())) {
                response.setErrors(lateTimeCheckDTOS.get(0).getErrorDTOS());
                response.setStatus(lateTimeCheckDTOS.get(0).getErrorDTOS().get(0).getErrorCode());
                response.setMessage(lateTimeCheckDTOS.get(0).getErrorDTOS().get(0).getMessage());

            } else {
                response.setData(lateTimeCheckDTOS);
            }
        } catch (IOException e) {
            response.setStatus(ErrorCode.ERROR_IO_EXCEPTION.getValue());
            response.setMessage(ErrorMessage.ERROR_IO_EXCEPTION);
        } catch (Exception e) {
            response.setStatus(ErrorCode.SYSTEM_ERROR.getValue());
            response.setMessage(ErrorCode.SYSTEM_ERROR.getDescription());
        }
        return response;
    }
}
