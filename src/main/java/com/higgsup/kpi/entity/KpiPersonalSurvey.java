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

    @JoinColumn(name = "rated_username", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser ratedUsername;

    @JoinColumn(name = "man_username", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser manUsername;

    @Basic
    @Column(name = "personal_point")
    private Float personalPoint;

    @Basic
    @Column(name = "year_month_id")
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

    public KpiUser getRatedUsername() {
        return ratedUsername;
    }

    public void setRatedUsername(KpiUser ratedUsername) {
        this.ratedUsername = ratedUsername;
    }

    public KpiUser getManUsername() {
        return manUsername;
    }

    public void setManUsername(KpiUser manUsername) {
        this.manUsername = manUsername;
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
