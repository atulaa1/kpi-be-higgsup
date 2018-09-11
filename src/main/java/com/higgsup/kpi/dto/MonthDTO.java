package com.higgsup.kpi.dto;

import java.sql.Date;

public class MonthDTO {

    private Integer id;
    private Date month;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }
}
