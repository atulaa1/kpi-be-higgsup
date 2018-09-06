package com.higgsup.kpi.entity;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "kpi_event")
public class KpiEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)

    private Integer id;
    @Basic
    @Column(name = "event_Name")
    private String eventName;

    @Basic
    @Column(name = "created_date")
    @CreationTimestamp
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date")
    @CreationTimestamp
    private Timestamp updatedDate;

    @Basic
    @Column(name = "address")
    private String address;

    @Basic
    @Column(name = "event_additional_config")
    private String eventAdditionalConfig;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "begin_date")
    private Timestamp beginDate;

    @Basic
    @Column(name = "end_date")
    private Timestamp endDate;

    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiGroup group;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kpiEvent", fetch = FetchType.LAZY)
    private List<KpiEventUser> kpiEventUserList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String name) {
        this.eventName = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public KpiGroup getGroup() {
        return group;
    }

    public void setGroup(KpiGroup group) {
        this.group = group;
    }

    public List<KpiEventUser> getKpiEventUserList() {
        return kpiEventUserList;
    }

    public void setKpiEventUserList(List<KpiEventUser> kpiEventUserList) {
        this.kpiEventUserList = kpiEventUserList;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
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

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventAdditionalConfig() {
        return eventAdditionalConfig;
    }

    public void setEventAdditionalConfig(String eventAdditionalConfig) {
        this.eventAdditionalConfig = eventAdditionalConfig;
    }
}
