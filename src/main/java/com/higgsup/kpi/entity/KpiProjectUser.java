package com.higgsup.kpi.entity;


import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_project_user")
public class KpiProjectUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private KpiEventUserPK kpiEventUserPK;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser kpiUser;

    @JoinColumn(name = "project_id", referencedColumnName = "id", insertable = false, updatable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiProject kpiProject;

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

    public KpiProject getKpiProject() {
        return kpiProject;
    }

    public void setKpiProject(KpiProject kpiProject) {
        this.kpiProject = kpiProject;
    }
}
