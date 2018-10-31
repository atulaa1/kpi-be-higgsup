package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeeFamePointDetailDTO {

    private FamePointDTO famePoint;

    private List<PointDTO> listFamePointInMonth;

    public FamePointDTO getFamePoint() {
        return famePoint;
    }

    public void setFamePoint(FamePointDTO famePoint) {
        this.famePoint = famePoint;
    }

    public List<PointDTO> getListFamePointInMonth() {
        return listFamePointInMonth;
    }

    public void setListFamePointInMonth(List<PointDTO> listFamePointInMonth) {
        this.listFamePointInMonth = listFamePointInMonth;
    }
}
