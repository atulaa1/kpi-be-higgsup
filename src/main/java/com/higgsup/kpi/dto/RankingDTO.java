package com.higgsup.kpi.dto;

public class RankingDTO {
    private Float totalPoint;
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
}
