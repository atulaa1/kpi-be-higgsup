package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "kpi_point_type")
public class KpiPointType {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

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

}
