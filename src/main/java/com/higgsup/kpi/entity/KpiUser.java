package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
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
    private String avatar;

    @Basic
    @Column(name = "birthday")
    private Date birthday;

    @Basic
    @Column(name = "number_phone")
    private String numberPhone;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "gmail")
    private String gmail;

    @Basic
    @Column(name = "skype")
    private String skype;

    @Basic
    @Column(name = "year_work")
    private Integer yearWork;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public Integer getYearWork() {
        return yearWork;
    }

    public void setYearWork(Integer yearWork) {
        this.yearWork = yearWork;
    }
}
