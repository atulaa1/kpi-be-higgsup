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

    @JoinColumn(name = "evaluation_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiEvaluation evaluation;

    @Basic
    @Column(name = "personal_point")
    private Float personalPoint;

    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiSurvey question;

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

    public KpiEvaluation getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(KpiEvaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Float getPersonalPoint() {
        return personalPoint;
    }

    public void setPersonalPoint(Float personalPoint) {
        this.personalPoint = personalPoint;
    }

    public KpiSurvey getQuestion() {
        return question;
    }

    public void setQuestion(KpiSurvey question) {
        this.question = question;
    }
}
