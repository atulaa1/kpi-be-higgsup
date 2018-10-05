package com.higgsup.kpi.glossary;

public enum PointValue {
    LATE_TIME_POINT(-3, "late time point"),
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
