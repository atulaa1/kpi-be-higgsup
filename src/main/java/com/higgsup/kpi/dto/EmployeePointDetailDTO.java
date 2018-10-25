package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeePointDetailDTO {
    private List<PointDetailDTO> pointDetailDTOs;

    private List<PointDTO> pointDTO;

    public List<PointDetailDTO> getPointDetailDTOs() {
        return pointDetailDTOs;
    }

    public void setPointDetailDTOs(List<PointDetailDTO> pointDetailDTOs) {
        this.pointDetailDTOs = pointDetailDTOs;
    }

    public List<PointDTO> getPointDTO() {
        return pointDTO;
    }

    public void setPointDTO(List<PointDTO> pointDTO) {
        this.pointDTO = pointDTO;
    }
}
