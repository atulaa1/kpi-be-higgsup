package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

public abstract class BaseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        if (Objects.nonNull(this.errorCode)) {
            return true;
        } else {
            return false;
        }
    }
}
