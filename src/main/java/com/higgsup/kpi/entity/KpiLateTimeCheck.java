package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_latetime_check")
public class KpiLateTimeCheck implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "late_times")
    private Integer lateTimes;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiUser user;

    @JoinColumn(name = "month_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private KpiYearMonth month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public KpiUser getUser() {
        return user;
    }

    public void setUser(KpiUser user) {
        this.user = user;
    }

    public KpiYearMonth getMonth() {
        return month;
    }

    public void setMonth(KpiYearMonth month) {
        this.month = month;
    }

    public Integer getLateTimes() {
        return lateTimes;
    }

    public void setLateTimes(Integer lateTimes) {
        this.lateTimes = lateTimes;
    }
}
