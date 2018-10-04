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

    @Basic
    @Column(name = "project_id")
    private KpiProject project;

    @Basic
    @JoinColumn(name = "rated_username")
    private KpiUser ratedUsername;

    @Basic
    @Column(name = "yearmonth")
    private Integer yearMonth;

    @Basic
    @Column(name = "project_point")
    private Float projectPoint;

    @Column(name = "man_username")
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


}
