package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "kpi_project_user")
public class KpiProjectUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;


    @JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiProject project;

    @Basic
    @Column(name = "rated_username")
    private String projectUser;

    @Basic
    @Column(name = "joined_date")
    @CreationTimestamp
    private Timestamp joinedDate;

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

    public String getProjectUser() {
        return projectUser;
    }

    public void setProjectUser(String projectUser) {
        this.projectUser = projectUser;
    }

    public Timestamp getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Timestamp joinedDate) {
        this.joinedDate = joinedDate;
    }
}
