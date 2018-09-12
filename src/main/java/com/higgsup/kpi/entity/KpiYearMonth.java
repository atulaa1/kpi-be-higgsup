package com.higgsup.kpi.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "kpi_year_month")
public class KpiYearMonth implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Basic
    @Column(name = "yearmonth")
    private Integer yearMonth;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(Integer yearMonth) {
        this.yearMonth = yearMonth;
    }
}
