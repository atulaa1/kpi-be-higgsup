package com.higgsup.kpi.glossary;

public enum ErrorCode {
    NOT_FIND(900, "not find"),
    PARAMETERS_IS_NOT_VALID(901, "parameter is not valid"),
    PARAMETERS_ALREADY_EXIST(932, "parameters already exist"),
    NOT_NULL(903, "parameter cannot null"),
    JSON_PROCESSING_EXCEPTION(904, "json processing exception"),
    DATA_EXIST(902, "data exist"),
    NOT_FILLING_ALL_INFORMATION(905, "not filling all information"),
    ALREADY_CREATED(906, "already created"),
    NOT_FIND_ITEM(928, "item does not existed"),
    NOT_FIND_GROUP_TYPE(929, "group type does not existed"),
    ERROR_IO_EXCEPTION(930, "error IOException"),
    ERROR_NO_SUCH_FIELD_EXCEPTION(930, "error no such field exception"),
    BEGIN_DATE_IS_NOT_AFTER_END_DATE(932, "begin date is not after end date"),
    CAN_NOT_UPDATE_EVENT(915, "cannot update"),
    SYSTEM_ERROR(999, "system error"),
    DATA_CAN_NOT_CHANGE(907, "data can not change"),
    TEAM_BUILDING_PRIZE_SCORE_CAN_NOT_NULL(931, "team building prize score can not be null"),
    INCORRECT_FILE_FORMAT(910, "incorrect file format"),
    INVALID_COLUMN_NAME(911, "invalid column name"),
    MIN_SESSION_MUST_BIGGER_THAN_ZERO(914, "minimum session must be bigger than zero"),
    INCORRECT_DATA(916, "incorrect data"),
    ALREADY_EVALUATED(917, "already evaluated"),
    HOST_CANNOT_CREATE_SEMINAR_SURVEY(918, "host cannot create any seminar survey"),
    NOT_ATTEND_EVENT(919, "not attend this event"),
    CANNOT_UPDATE(915, "cannot update");

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
