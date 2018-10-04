package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.security.Timestamp;

@Entity
@Table(name = "kpi_project_user")
public class KpiProjectUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "project_id")
    private KpiProject project;

    @Basic
    @Column(name = "rated_username")
    private KpiUser ratedUsername;

    @Basic
    @Column(name = "joined_date")
    private Timestamp joindDate;

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

    public KpiUser getRatedUsername() {
        return ratedUsername;
    }

    public void setRatedUsername(KpiUser ratedUsername) {
        this.ratedUsername = ratedUsername;
    }

    public Timestamp getJoindDate() {
        return joindDate;
    }

    public void setJoindDate(Timestamp joindDate) {
        this.joindDate = joindDate;
    }
}
