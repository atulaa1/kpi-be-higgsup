package com.higgsup.kpi.dto;

import com.higgsup.kpi.entity.KpiEvent;
import com.higgsup.kpi.entity.KpiUser;

public class EventUserDTO {
    private KpiUser kpiUser;

    private KpiEvent kpiEvent;

    private Integer type;

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
}
