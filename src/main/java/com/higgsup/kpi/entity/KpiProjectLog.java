package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_project_log")
public class KpiProjectLog implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiProject project;

    @JoinColumn(name = "evaluation_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiEvaluation evaluationId;

    @Basic
    @Column(name = "project_point")
    private Float projectPoint;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiProject getProject() {
        return project;
    }

    public KpiEvaluation getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(KpiEvaluation evaluationId) {
        this.evaluationId = evaluationId;
    }

    public void setProject(KpiProject project) {
        this.project = project;
    }

    public Float getProjectPoint() {
        return projectPoint;
    }

    public void setProjectPoint(Float projectPoint) {
        this.projectPoint = projectPoint;
    }
}
