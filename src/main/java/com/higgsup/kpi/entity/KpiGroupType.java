package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "kpi_group_type")
public class KpiGroupType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "groupType", fetch = FetchType.LAZY)
    private List<KpiGroup> kpiGroupList;

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

    public List<KpiGroup> getKpiGroupList() {
        return kpiGroupList;
    }

    public void setKpiGroupList(List<KpiGroup> kpiGroupList) {
        this.kpiGroupList = kpiGroupList;
    }
}
