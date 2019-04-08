package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_event_user")
public class KpiEventUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser kpiUser;

    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiEvent kpiEvent;

    @Column(name = "type")
    private Integer type;

    @Column(name = "status")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiUser getKpiUser() {
        return kpiUser;
    }

    public void setKpiUser(KpiUser kpiUser) {
        this.kpiUser = kpiUser;
    }

    public KpiEvent getKpiEvent() {
        return kpiEvent;
    }

    public void setKpiEvent(KpiEvent kpiEvent) {
        this.kpiEvent = kpiEvent;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
