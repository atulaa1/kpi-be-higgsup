package com.higgsup.kpi.dto;

public class RankingDTO {
    private Float totalPoint;
    private Float famedPoint;
    private UserDTO employee;

    public Float getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Float totalPoint) {
        this.totalPoint = totalPoint;
    }

    public UserDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserDTO employee) {
        this.employee = employee;
    }

    public Float getFamedPoint() {
        return famedPoint;
    }

    public void setFamedPoint(Float famedPoint) {
        this.famedPoint = famedPoint;
    }
}
