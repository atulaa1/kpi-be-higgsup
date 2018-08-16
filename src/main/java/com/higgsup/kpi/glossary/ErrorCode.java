package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "not find"),
    NOT_FIND_USER(980,"Not find user"),
    PARAMETERS_IS_NOT_VALID(901, "parameter is not valid"),
    PARAMETERS_ALREADY_EXIST(932,"parameters already exist"),
    POINT_HOST_NOT_LARGER_THAN_POINT_MEMBER(945,"point host not larger than point member"),
    POINT_MEMBER_NOT_LARGER_THAN_POINT_LISTENER(946,"point member not larger than point listener");
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
