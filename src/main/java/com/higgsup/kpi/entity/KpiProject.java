package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "kpi_project")
public class KpiProject implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "active", columnDefinition = "TINYINT default 1")
    private Integer active;

    @Basic
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date")
    @CreationTimestamp
    private Timestamp updatedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<KpiProjectUser> ratedUserList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updateDate) {
        this.updatedDate = updateDate;
    }

    public List<KpiProjectUser> getRatedUserList() {
        return ratedUserList;
    }

    public void setRatedUserList(List<KpiProjectUser> ratedUserList) {
        this.ratedUserList = ratedUserList;
    }
}
