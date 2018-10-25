package com.higgsup.kpi.dto;

public class PointDetailDTO {
    private EventDTO event;

    private YearMonthDTO yearMonth;

    private Float point;

    private Integer pointType;

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public YearMonthDTO getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonthDTO yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }
}
