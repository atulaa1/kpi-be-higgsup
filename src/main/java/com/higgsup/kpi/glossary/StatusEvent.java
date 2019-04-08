package com.higgsup.kpi.glossary;

public enum StatusEvent {
    WAITING(1, "waiting"),
    CONFIRMED(2, "confirmed"),
    CANCEL(3, "cancel");

    private Integer value;
    private String content;

    StatusEvent(Integer value, String content) {
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
