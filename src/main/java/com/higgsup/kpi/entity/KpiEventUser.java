package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_event_user")
public class KpiEventUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    protected KpiEventUserPK kpiEventUserPK;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser kpiUser;

    @JoinColumn(name = "event_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiEvent kpiEvent;

    @Column(name = "is_host")
    private Integer isHost;

    public KpiEventUserPK getKpiEventUserPK() {
        return kpiEventUserPK;
    }

    public void setKpiEventUserPK(KpiEventUserPK kpiEventUserPK) {
        this.kpiEventUserPK = kpiEventUserPK;
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

    public Integer getIsHost() {
        return isHost;
    }

    public void setIsHost(Integer isHost) {
        this.isHost = isHost;
    }
}
