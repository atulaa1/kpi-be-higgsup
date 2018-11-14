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

    @JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiProject project;

    @JoinColumn(name = "rated_username", referencedColumnName = "rated_username", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiProjectUser ratedUsername;

    @Basic
    @Column(name = "year_month_id")
    private Integer yearMonth;

    @Basic
    @Column(name = "project_point")
    private Float projectPoint;

    @JoinColumn(name = "man_username", referencedColumnName = "user_name", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser manUsername;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiProject getProject() {
        return project;
    }

    public void setProject(KpiProject project) {
        this.project = project;
    }


    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }

    public Float getProjectPoint() {
        return projectPoint;
    }

    public void setProjectPoint(Float projectPoint) {
        this.projectPoint = projectPoint;
    }

    public KpiUser getManUsername() {
        return manUsername;
    }

    public void setManUsername(KpiUser manUsername) {
        this.manUsername = manUsername;
    }

    public KpiProjectUser getRatedUsername() {
        return ratedUsername;
    }

    public void setRatedUsername(KpiProjectUser ratedUsername) {
        this.ratedUsername = ratedUsername;
    }
}
