package com.higgsup.kpi.dto;

import java.util.List;

public class EmployeePointDetailDTO {
    private List<PointDetailDTO> pointDetailList;

    private PointDTO pointTotal;

    private YearMonthDTO yearMonth;

    public List<PointDetailDTO> getPointDetailList() {
        return pointDetailList;
    }

    public void setPointDetailList(List<PointDetailDTO> pointDetailList) {
        this.pointDetailList = pointDetailList;
    }

    public PointDTO getPointTotal() {
        return pointTotal;
    }

    public void setPointTotal(PointDTO pointTotal) {
        this.pointTotal = pointTotal;
    }

    public YearMonthDTO getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonthDTO yearMonth) {
        this.yearMonth = yearMonth;
    }
}
