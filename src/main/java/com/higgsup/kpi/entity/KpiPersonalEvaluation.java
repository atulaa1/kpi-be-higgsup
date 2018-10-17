package com.higgsup.kpi.entity;


import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "kpi_personal_evaluation")
public class KpiPersonalEvaluation {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "survey_name")
    private String surveyName;

    @Basic
    @Column(name = "evaluating_time")
    private Timestamp evaluatingTime;

    @JoinColumn(name = "evaluator_username", referencedColumnName = "user_name")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser evaluator;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurveryName() {
        return surveyName;
    }

    public void setSurveryName(String surveryName) {
        this.surveyName = surveryName;
    }

    public Timestamp getEvaluatingTime() {
        return evaluatingTime;
    }

    public void setEvaluatingTime(Timestamp evaluatingTime) {
        this.evaluatingTime = evaluatingTime;
    }

    public KpiUser getEvaluator() {
        return evaluator;
    }

    public void setEvaluator(KpiUser evaluator) {
        this.evaluator = evaluator;
    }
}
