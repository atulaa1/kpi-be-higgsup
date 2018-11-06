package com.higgsup.kpi.dto;

public class FamePointDTO {

    private Integer id;

    private UserDTO user;

    private Float famePoint;

    private Integer year;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public Float getFamePoint() {
        return famePoint;
    }

    public void setFamePoint(Float famePoint) {
        this.famePoint = famePoint;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
