package com.higgsup.kpi.entity;

import javax.persistence.*;

@Entity
@Table(name = "kpi_point_detail")
public class KpiPointDetail {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @JoinColumn(name = "user_name", referencedColumnName = "user_name")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private KpiUser user;

    @JoinColumn(name = "event_id", referencedColumnName = "id")
    @OneToOne(fetch = FetchType.LAZY)
    private KpiEvent event;

    @Column(name = "point")
    private Float point;

    @Column(name = "point_type")
    private Integer pointType;

    @JoinColumn(name = "year_month_id", referencedColumnName = "id")
    private Integer yearMonthId;


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

    public KpiEvent getEvent() {
        return event;
    }

    public void setEvent(KpiEvent event) {
        this.event = event;
    }

    public Integer getPointType() {
        return pointType;
    }

    public void setPointType(Integer pointType) {
        this.pointType = pointType;
    }

    public Float getPoint() {
        return point;
    }

    public void setPoint(Float point) {
        this.point = point;
    }

    public Integer getYearMonthId() {
        return yearMonthId;
    }

    public void setYearMonthId(Integer yearMonthId) {
        this.yearMonthId = yearMonthId;
    }
}
