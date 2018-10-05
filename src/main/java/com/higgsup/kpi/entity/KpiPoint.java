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


    @JoinColumn(name = "rated_username", referencedColumnName = "user_name", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser ratedUser;

    @Basic
    @Column(name = "rule_point")
    private Float rulePoint;

    @Basic
    @Column(name = "club_point")
    private Float clubPoint;

    @Basic
    @Column(name = "normal_seminar_point")
    private Float normalSeminarPoint;

    @Basic
    @Column(name = "weekend_seminar_point")
    private Float weekendSeminarPoint;

    @Basic
    @Column(name = "support_point")
    private Float supportPoint;

    @Basic
    @Column(name = "teambuilding_point")
    private Float teambuildingPoint;

    @Basic
    @Column(name = "personal_point")
    private Float personalPoint;

    @Basic
    @Column(name = "project_point")
    private Float projectPoint;

    @Basic
    @Column(name = "total_point")
    private Float totalPoint;

    @Basic
    @Column(name = "year_month")
    private Integer yearMonth;

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

    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }
}
