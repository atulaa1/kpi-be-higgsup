package com.higgsup.kpi.glossary;

public enum EventUserType {
    HOST(1, "host"),
    MEMBER(2, "member"),
    LISTEN(3, "listen");

    private Integer value;
    private String content;

    EventUserType(Integer value, String content) {
        this.value = value;
        this.content = content;
    }

    public static EventUserType getEventUserType(Integer value) {
        for (EventUserType eventUserType : EventUserType.values()) {
            if (eventUserType.getValue().equals(value)) {
                return eventUserType;
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
