package com.higgsup.kpi.glossary;

public enum ProjectStatus {
    ACTIVE(1, "active"),
    DEACTIVE(2, "deactive"),
    EVALUATED(3, "evaluated");

    private Integer value;
    private String content;

    ProjectStatus(Integer value, String content) {
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
