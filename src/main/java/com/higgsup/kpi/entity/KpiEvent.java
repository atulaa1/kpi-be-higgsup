package com.higgsup.kpi.entity;

import com.higgsup.kpi.dto.KpiGroup;

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
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "status")
    private String status;


    @Basic
    @Column(name = "begin_date")
    private Timestamp beginDate;


    @Basic
    @Column(name = "end_date")
    private Timestamp endDate;

    @JoinColumn(name = "group_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private com.higgsup.kpi.dto.KpiGroup groupId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "kpiEvent", fetch = FetchType.LAZY)
    private List<KpiEventUser> kpiEventUserList;

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

    public com.higgsup.kpi.dto.KpiGroup getGroupId() {
        return groupId;
    }

    public void setGroupId(KpiGroup groupId) {
        this.groupId = groupId;
    }

    public List<KpiEventUser> getKpiEventUserList() {
        return kpiEventUserList;
    }

    public void setKpiEventUserList(List<KpiEventUser> kpiEventUserList) {
        this.kpiEventUserList = kpiEventUserList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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
}
