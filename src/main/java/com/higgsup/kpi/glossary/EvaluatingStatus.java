package com.higgsup.kpi.glossary;

public enum EvaluatingStatus {
    UNFINISHED(0, "unfinished"),
    FINISH(1, "finish");

    private Integer value;

    private String content;

    EvaluatingStatus(Integer value, String content) {
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
