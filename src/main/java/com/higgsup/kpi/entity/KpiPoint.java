package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_point")
public class KpiPoint implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    @OneToMany(fetch = FetchType.LAZY)
    private KpiEventUser eventUser;

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
    @Column(name = "weekend_seminar")
    private Float weekendSeminarPoint;

    @Basic
    @Column(name = "support_point")
    private Float supportPoint;

    @Basic
    @Column(name = "teambuilding_point")
    private Float teambuildingPoint;

    @Basic
    @Column(name = "evaluating_point")
    private Float evaluatingPointByMan;

    @Basic
    @Column(name = "total_point")
    private Float totalPoint;

    @JoinColumn(name = "year_month_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiYearMonth yearMonth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiEventUser getEventUser() {
        return eventUser;
    }

    public void setEventUser(KpiEventUser eventUser) {
        this.eventUser = eventUser;
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

    public Float getEvaluatingPointByMan() {
        return evaluatingPointByMan;
    }

    public void setEvaluatingPointByMan(Float evaluatingPointByMan) {
        this.evaluatingPointByMan = evaluatingPointByMan;
    }

    public Float getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(Float totalPoint) {
        this.totalPoint = totalPoint;
    }

    public KpiYearMonth getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(KpiYearMonth yearMonth) {
        this.yearMonth = yearMonth;
    }
}
