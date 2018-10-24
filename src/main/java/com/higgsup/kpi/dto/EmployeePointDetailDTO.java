package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeePointDetailDTO {
    private List<PointDetailDTO> pointDetailDTOs;

    private PointDTO pointDTO;

    public List<PointDetailDTO> getPointDetailDTOs() {
        return pointDetailDTOs;
    }

    public void setPointDetailDTOs(List<PointDetailDTO> pointDetailDTOs) {
        this.pointDetailDTOs = pointDetailDTOs;
    }

    public PointDTO getPointDTO() {
        return pointDTO;
    }

    public void setPointDTO(PointDTO pointDTO) {
        this.pointDTO = pointDTO;
    }
}
