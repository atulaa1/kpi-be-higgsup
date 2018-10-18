package com.higgsup.kpi.dto;

public class PointDetailDTO {
    private EventDTO event;

    private Integer yearMonthId;

    private Float point;

    private PointTypeDTO pointType;

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public Integer getYearMonth() {
        return yearMonthId;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonthId = yearMonth;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public PointTypeDTO getPointType() {
        return pointType;
    }

    public void setPointType(PointTypeDTO pointType) {
        this.pointType = pointType;
    }
}
