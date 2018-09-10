package com.higgsup.kpi.entity;

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
    @Column(name = "event_name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "status")
    private Integer status;

    @Basic
    @Column(name = "created_date")
    private Timestamp createdDate;

    @Basic
    @Column(name = "begin_date")
    private Timestamp beginDate;

    @Basic
    @Column(name = "end_date")
    private Timestamp endDate;

    @Basic
    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiGroup group;

    @Basic
    @Column(name = "address")
    private String address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kpiEvent", fetch = FetchType.LAZY)
    private List<KpiEventUser> participants;

    @Basic
    @Column(name = "event_additional_config")
    private String additionalConfig;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public KpiGroup getGroup() {
        return group;
    }

    public void setGroup(KpiGroup group) {
        this.group = group;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<KpiEventUser> getParticipants() {
        return participants;
    }

    public void setParticipants(List<KpiEventUser> participants) {
        this.participants = participants;
    }

    public String getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(String additionalConfig) {
        this.additionalConfig = additionalConfig;
    }
}
