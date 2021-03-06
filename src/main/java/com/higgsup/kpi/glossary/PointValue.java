package com.higgsup.kpi.glossary;

public enum PointValue {
    LATE_TIME_POINT(-3, "late time point"),
    MAX_CLUB_POINT(18, "max club point"),
    MAX_WEEKEND_SEMINAR_POINT(30, "max weekend seminar point"),
    MAX_PROJECT_POINT(20, "max project point"),
    MAX_INDIVIDUAL_POINT(72, "max individual point"),
    MAX_NORMAL_SEMINAR_POINT(24, "max normal seminar point"),
    MAX_SUPPORT_POINT(24, "max support point"),
    DEFAULT_EFFECTIVE_POINT(3, "default effective point"),
    BEST_EMPLOYEE_OF_THE_MONTH_FAME_POINT(15, "best employee of the month fame point"),
    EMPLOYEE_OF_THE_MONTH_I_FAME_POINT(10, "employee of the month I"),
    EMPLOYEE_OF_THE_MONTH_II_FAME_POINT(5, "employee of the month II"),
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
