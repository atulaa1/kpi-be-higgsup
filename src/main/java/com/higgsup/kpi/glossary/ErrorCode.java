package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "Not find"),
    PARAMETERS_IS_NOT_VALID(901, "Parameter is not valid"),

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
