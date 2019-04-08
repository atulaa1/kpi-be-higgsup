package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "kpi_seminar_survey")
public class KpiSeminarSurvey {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "evaluating_username", referencedColumnName = "user_name")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiUser evaluatingUsername;

    @JoinColumn(name = "evaluated_username", referencedColumnName = "user_name")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiUser evaluatedUsername;

    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiEvent event;

    @Basic
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Basic
    @Column(name = "rating")
    private Integer rating;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiUser getEvaluatingUsername() {
        return evaluatingUsername;
    }

    public void setEvaluatingUsername(KpiUser evaluatingUsername) {
        this.evaluatingUsername = evaluatingUsername;
    }

    public KpiUser getEvaluatedUsername() {
        return evaluatedUsername;
    }

    public void setEvaluatedUsername(KpiUser evaluatedUsername) {
        this.evaluatedUsername = evaluatedUsername;
    }

    public KpiEvent getEvent() {
        return event;
    }

    public void setEvent(KpiEvent event) {
        this.event = event;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
