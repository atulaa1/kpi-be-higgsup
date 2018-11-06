package com.higgsup.kpi.glossary;

public enum Title {
    NOT_HAVE_TITLE(0, "not have title"),
    BEST_EMPLOYEE_OF_THE_MONTH(1, "best employee of the month"),
    EMPLOYEE_OF_THE_MONTH_I(2, "employee of the month I"),
    EMPLOYEE_OF_THE_MONTH_II(3, "employee of the month II"),
    BEST_EMPLOYEE_OF_THE_YEAR(4, "best employee of the year"),
    EMPLOYEE_OF_THE_YEAR(5, "employee of the year");

    private Integer value;
    private String content;

    Title(Integer value, String content) {
        this.value = value;
        this.content = content;
    }

    public static StatusEvent getStatusEvent(Integer value) {
        for (StatusEvent statusEvent : StatusEvent.values()) {
            if (statusEvent.getValue().equals(value)) {
                return statusEvent;
            }
        }
        return null;
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
