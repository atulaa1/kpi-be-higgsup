package com.higgsup.kpi.glossary;

public enum PointType {
    RULE_POINT(1, "rule point"),
    CLUB_POINT(2, "club point"),
    NORMAL_SEMINAR_POINT(3, "normal seminar point"),
    SATURDAY_SEMINAR_POINT(4, "saturday seminar point"),
    SUPPORT_POINT(5, "support point"),
    TEAMBUILDING_POINT(6, "team building point"),
    EVALUATE_POINT(7, "evaluate point");

    private Integer value;

    private String content;

    PointType(Integer value, String content) {
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
