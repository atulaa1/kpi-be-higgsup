package com.higgsup.kpi.entity;

import javax.persistence.*;

@Entity
@Table(name = "kpi_personal_survey")
public class KpiPersonalSurvey {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "rated_username", referencedColumnName = "user_name", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser ratedUser;

    @JoinColumn(name = "man_username", referencedColumnName = "user_name", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser evaluator;

    @Basic
    @Column(name = "personal_point")
    private Float personalPoint;

    @Basic
    @Column(name = "year_month")
    private Integer yearMonthId;

    @Basic
    @Column(name = "survey_id")
    private Integer surveyId;

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

    public KpiUser getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(KpiUser evaluator) {
        this.evaluator = evaluator;
    }

    public Float getPersonalPoint() {
        return personalPoint;
    }

    public void setPersonalPoint(Float personalPoint) {
        this.personalPoint = personalPoint;
    }

    public Integer getYearMonthId() {
        return yearMonthId;
    }

    public void setYearMonthId(Integer yearMonthId) {
        this.yearMonthId = yearMonthId;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }
}
