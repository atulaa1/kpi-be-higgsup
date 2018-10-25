package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeeFamePointDetailDTO {

    private List<FamePointDTO> famePointDTOs;

    private List<PointDTO> pointDTOs;

    public List<FamePointDTO> getFamePointDTOs() {
        return famePointDTOs;
    }

    public void setFamePointDTOs(List<FamePointDTO> famePointDTOs) {
        this.famePointDTOs = famePointDTOs;
    }

    public List<PointDTO> getPointDTOs() {
        return pointDTOs;
    }

    public void setPointDTOs(List<PointDTO> pointDTOs) {
        this.pointDTOs = pointDTOs;
    }
}
