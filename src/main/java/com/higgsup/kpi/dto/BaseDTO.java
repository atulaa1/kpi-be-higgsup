package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public abstract class BaseDTO {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer errorCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorDTO> errorDTOS;

    public List<ErrorDTO> getErrorDTOS() {
        return errorDTOS;
    }

    public void setErrorDTOS(List<ErrorDTO> errorDTOS) {
        this.errorDTOS = errorDTOS;
    }

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

}
