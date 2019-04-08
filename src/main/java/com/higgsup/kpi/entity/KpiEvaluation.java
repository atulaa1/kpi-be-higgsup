package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "kpi_evaluation")
public class KpiEvaluation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Basic
    @Column(name = "evaluation_name")
    private String evaluationName;

    @JoinColumn(name = "man_username", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser manUser;

    @JoinColumn(name = "year_month_id", referencedColumnName = "id")
    private Integer yearMonthId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getEvaluationName() {
        return evaluationName;
    }

    public void setEvaluationName(String evaluationName) {
        this.evaluationName = evaluationName;
    }

    public KpiUser getManUser() {
        return manUser;
    }

    public void setManUser(KpiUser manUser) {
        this.manUser = manUser;
    }

    public Integer getYearMonthId() {
        return yearMonthId;
    }

    public void setYearMonthId(Integer yearMonthId) {
        this.yearMonthId = yearMonthId;
    }
}
