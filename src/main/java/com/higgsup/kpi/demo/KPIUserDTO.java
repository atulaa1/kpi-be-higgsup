package com.higgsup.kpi.demo;

import com.higgsup.kpi.entity.KpiEventUser;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dungpx on 10/4/2018.
 */
public class KPIUserDTO {

    public KPIUserDTO() {
    }

    public KPIUserDTO(String firstName, String lastName, String fullName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fullName = fullName;
        this.email = email;
    }

    private String userName;
    private Timestamp name;


    private Integer active;

    private String firstName;

    private String lastName;


    private String fullName;


    private String email;


    private String avatar;


    private Date birthday;


    private String numberPhone;


    private String address;


    private String gmail;


    private String skype;


    private Date dateStartWork;


    private List<KPIEventUserDTO> kpiEventUserList;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Date getDateStartWork() {
        return dateStartWork;
    }

    public void setDateStartWork(Date dateStartWork) {
        this.dateStartWork = dateStartWork;
    }

    public List<KPIEventUserDTO> getKpiEventUserList() {
        return kpiEventUserList;
    }

    public void setKpiEventUserList(List<KPIEventUserDTO> kpiEventUserList) {
        this.kpiEventUserList = kpiEventUserList;
    }
}
