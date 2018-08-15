package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "not find"),
    PARAMETERS_IS_NOT_VALID(901, "parameter is not valid"),

    INVALIDATED_FIRST_PRIZE(920, "invalidated first prize"),
    INVALIDATED_SECOND_PRIZE(921, "invalidated second prize"),
    INVALIDATED_THIRD_PRIZE(922, "invalidated third prize"),
    INVALIDATED_ORGNIZERS_PRIZE(923, "invalidated orgnizers record"),
    FIRST_PRIZE_NOT_LARGER_THAN_SECOND_PRIZE(924, "first prize has to large than second prize"),
    SECOND_PRIZE_NOT_LARGER_THAN_THIRD_PRIZE(925, "invalidated third prize"),
    DUPLICATED_ITEM(926,"duplicated item");

    private Integer value;
    private String content;

    ErrorCode(Integer value, String content) {
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
