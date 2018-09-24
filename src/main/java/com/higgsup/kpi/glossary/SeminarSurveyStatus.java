package com.higgsup.kpi.glossary;

public enum SeminarSurveyStatus {
    UNFINISHED(0, "unfinished"),
    FINISHED(1, "finished");

    private Integer value;
    private String content;

    SeminarSurveyStatus(Integer value, String content) {
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
