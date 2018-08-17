package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "not find"),
    NOT_FIND_USER(980,"Not find user"),
    PARAMETERS_IS_NOT_VALID(901, "parameter is not valid"),
    DUPLICATED_ITEM(930,"duplicated item"),
    PARAMETERS_ALREADY_EXIST(932,"parameters already exist"),
    NO_LARGER_THAN(940,"no larger than"),
    NOT_NULL(903, "parameter is not null"),
    JSON_PROCESSING_EXCEPTION(904, "json processing exception"),
    DATA_EXIST(902, "data exist"),
    NOT_FILLING_ALL_INFORMATION(905, "Not filling all information"),
    ALREADY_CREATED(906, "Already created");

    private Integer value;
    private String description;

    ErrorCode(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
