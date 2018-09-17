package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.LateTimeCheckDTO;
import com.higgsup.kpi.entity.KpiLateTimeCheck;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface LateTimeCheckService {
    List<KpiLateTimeCheck> createDataNewMonthOrUpdate();

    List<LateTimeCheckDTO> getAllLateTimeCheckCurrent();
    List<LateTimeCheckDTO> processExcelFile(MultipartFile file) throws IOException;
}
