package com.higgsup.kpi.dto;

public class PointDTO extends BaseDTO {

    private Integer id;

    private UserDTO ratedUser;

    private Float rulePoint;

    private Float clubPoint;

    private Float normalSeminarPoint;

    private Float weekendSeminarPoint;

    private Float supportPoint;

    private Float teambuildingPoint;

    private Float personalPoint;

    private Float projectPoint;

    private Float totalPoint;

    private Integer yearMonth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserDTO getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(UserDTO ratedUser) {
        this.ratedUser = ratedUser;
    }

    public Float getRulePoint() {
        return rulePoint;
    }

    public void setRulePoint(Float rulePoint) {
        this.rulePoint = rulePoint;
    }

    public Float getClubPoint() {
        return clubPoint;
    }

    public void setClubPoint(Float clubPoint) {
        this.clubPoint = clubPoint;
    }

    public Float getNormalSeminarPoint() {
        return normalSeminarPoint;
    }

    public void setNormalSeminarPoint(Float normalSeminarPoint) {
        this.normalSeminarPoint = normalSeminarPoint;
    }

    public Float getWeekendSeminarPoint() {
        return weekendSeminarPoint;
    }

    public void setWeekendSeminarPoint(Float weekendSeminarPoint) {
        this.weekendSeminarPoint = weekendSeminarPoint;
    }

    public Float getSupportPoint() {
        return supportPoint;
    }

    public void setSupportPoint(Float supportPoint) {
        this.supportPoint = supportPoint;
    }

    public Float getTeambuildingPoint() {
        return teambuildingPoint;
    }

    public void setTeambuildingPoint(Float teambuildingPoint) {
        this.teambuildingPoint = teambuildingPoint;
    }

    public Float getPersonalPoint() {
        return personalPoint;
    }

    public void setPersonalPoint(Float personalPoint) {
        this.personalPoint = personalPoint;
    }

    public Float getProjectPoint() {
        return projectPoint;
    }

    public void setProjectPoint(Float projectPoint) {
        this.projectPoint = projectPoint;
    }

    public Float getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Float totalPoint) {
        this.totalPoint = totalPoint;
    }

    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }
}
