package com.higgsup.kpi.glossary;

public enum PointValue {
    LATE_TIME_POINT(-3, "late time point"),
    MAX_CLUB_POINT(18, "max club point"),
    MAX_WEEKEND_SEMINAR_POINT(30, "max weekend seminar point"),
    MAX_PROJECT_POINT(20, "max project point"),
    MAX_INDIVIDUAL_POINT(72, "max individual point"),
    FULL_RULE_POINT(24, "full rule point");


    private Integer value;

    private String content;

    PointValue(Integer value, String content) {
        this.value = value;
        this.content = content;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
