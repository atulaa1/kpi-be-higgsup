package com.higgsup.kpi.dto;

public class BestPerformancesDTO {
    Integer yearMonth;
    UserDTO employee;

    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }

    public UserDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserDTO employee) {
        this.employee = employee;
    }
}
