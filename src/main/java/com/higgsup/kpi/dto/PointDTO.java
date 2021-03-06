package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
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

    private YearMonthDTO yearMonth;

    private Integer title;

    private Float famedPoint;

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

    public YearMonthDTO getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(YearMonthDTO yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Integer getTitle() {
        return title;
    }

    public void setTitle(Integer title) {
        this.title = title;
    }

    public Float getFamedPoint() {
        return famedPoint;
    }

    public void setFamedPoint(Float famedPoint) {
        this.famedPoint = famedPoint;
    }
}
