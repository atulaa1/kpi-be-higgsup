package com.higgsup.kpi.demo;

/**
 * Created by dungpx on 10/4/2018.
 */
public class KPIEventUserDTO {


    public KPIEventUserDTO() {
    }

    public KPIEventUserDTO(Integer type, Integer status) {
        this.type = type;
        this.status = status;
    }

    private KPIEventDTO kpiEvent;

    private Integer type;

    private Integer status;

    public KPIEventDTO getKpiEvent() {
        return kpiEvent;
    }

    public void setKpiEvent(KPIEventDTO kpiEvent) {
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
