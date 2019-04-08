package com.higgsup.kpi.entity;

import javax.persistence.*;

@Entity
@Table(name = "kpi_fame_point")
public class KpiFamePoint {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "username", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser user;

    @Basic
    @Column(name = "fame_point")
    private Float famePoint;

    @Column(name = "year")
    private Integer year;

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

    public Float getFamePoint() {
        return famePoint;
    }

    public void setFamePoint(Float famePoint) {
        this.famePoint = famePoint;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
