package com.higgsup.kpi.entity;

import javax.persistence.*;

@Entity
@Table(name = "kpi_point")
public class KpiPoint {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;


    @JoinColumn(name = "rated_username", referencedColumnName = "user_name")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser ratedUser;

    @Basic
    @Column(name = "rule_point")
    private Float rulePoint = 0f;

    @Basic
    @Column(name = "club_point")
    private Float clubPoint = 0f;

    @Basic
    @Column(name = "normal_seminar_point")
    private Float normalSeminarPoint = 0f;

    @Basic
    @Column(name = "weekend_seminar_point")
    private Float weekendSeminarPoint = 0f;

    @Basic
    @Column(name = "support_point")
    private Float supportPoint = 0f;

    @Basic
    @Column(name = "teambuilding_point")
    private Float teambuildingPoint = 0f;

    @Basic
    @Column(name = "personal_point")
    private Float personalPoint = 0f;

    @Basic
    @Column(name = "project_point")
    private Float projectPoint = 0f;

    @Basic
    @Column(name = "total_point")
    private Float totalPoint = 0f;

    @Basic
    @Column(name = "famed_point")
    private Float famedPoint = 0f;

    @JoinColumn(name = "year_month_id", referencedColumnName = "id", columnDefinition="int(10) default 0")
    private Integer yearMonthId;

    @Column(name = "title")
    private Integer title;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiUser getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(KpiUser ratedUser) {
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

    public Integer getYearMonthId() {
        return yearMonthId;
    }

    public void setYearMonthId(Integer yearMonthId) {
        this.yearMonthId = yearMonthId;
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
