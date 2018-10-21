package com.higgsup.kpi.glossary;

public enum RankingType {
    NORMAL_POINT_RANKING(1, "normal point ranking"),
    FAMED_POINT_RANKING(2, "famed point ranking");

    private Integer value;
    private String content;

    RankingType(Integer value, String content) {
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
