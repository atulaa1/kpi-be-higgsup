package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "kpi_user")
public class KpiUser implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "user_name")
    private String userName;

    @Basic
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp name;
    @Basic
    @Column(name = "active")
    private Integer active;

    @Basic
    @Column(name = "first_name")
    private String firstName;

    @Basic
    @Column(name = "last_name")
    private String lastName;

    @Basic
    @Column(name = "email")
    private String email;

    @Basic
    @Column(name = "avatar_url")
    private String avatarUrl;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kpiUser", fetch = FetchType.LAZY)
    private List<KpiEventUser> kpiEventUserList;

    public List<KpiEventUser> getKpiEventUserList() {
        return kpiEventUserList;
    }

    public void setKpiEventUserList(List<KpiEventUser> kpiEventUserList) {
        this.kpiEventUserList = kpiEventUserList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Timestamp getName() {
        return name;
    }

    public void setName(Timestamp name) {
        this.name = name;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
