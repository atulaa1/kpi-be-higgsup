package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "not find"),
    PARAMETERS_IS_NOT_VALID(901, "parameter is not valid"),
    PARAMETERS_ALREADY_EXIST(902,"parameters already exist"),
    DATA_EXIST(902, "data exist"),
    NOT_NULL(903, "parameter is not null"),
    JSON_PROCESSING_EXCEPTION(904, "json processing exception"),
    NOT_FILLING_ALL_INFORMATION(905, "Not filling all information"),
    ALREADY_CREATED(906, "Already created"),
    DUPLICATED_ITEM(926,"Duplicated item"),
    DO_NOT_EXISTED_ITEM(927, "This team building activity do not existed"),
    NOT_FIND_ITEM(928,"Item does not existed"),
    NOT_FIND_GROUP_TYPE(929,"Item does not existed");

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
