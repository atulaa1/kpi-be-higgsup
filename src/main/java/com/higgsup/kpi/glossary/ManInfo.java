package com.higgsup.kpi.glossary;

public enum ManInfo {

    NUMBER_OF_MAN(3, "number of man");

    private Integer value;
    private String content;


    ManInfo(Integer value, String content) {
        this.value = value;
        this.content = content;
    }

    public Integer getValue() {
        return value;
    }

    public String getContent() {
        return content;
    }
}
