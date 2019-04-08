package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Response<T> {
    @JsonProperty("status_code")
    private Integer status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ErrorDTO> errors;

    public Response() {
    }

    public Response(Integer status) {
        this.status = status;
    }

    public List<ErrorDTO> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorDTO> errors) {
        this.errors = errors;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
