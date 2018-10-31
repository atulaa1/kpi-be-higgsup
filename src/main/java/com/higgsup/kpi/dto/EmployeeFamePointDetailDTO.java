package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeeFamePointDetailDTO {

    private FamePointDTO famePoint;

    private List<PointDTO> listFamePointInYear;

    public FamePointDTO getFamePoint() {
        return famePoint;
    }

    public void setFamePoint(FamePointDTO famePoint) {
        this.famePoint = famePoint;
    }

    public List<PointDTO> getListFamePointInYear() {
        return listFamePointInYear;
    }

    public void setListFamePointInYear(List<PointDTO> listFamePointInYear) {
        this.listFamePointInYear = listFamePointInYear;
    }
}
