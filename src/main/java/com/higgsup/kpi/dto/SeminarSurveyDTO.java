package com.higgsup.kpi.dto;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.sql.Timestamp;

public class SeminarSurveyDTO extends BaseDTO {

    private UserDTO evaluatingUsername;

    private UserDTO evaluatedUsername;

    private Integer rating;
    @JsonFormat(pattern = "dd-MM-yyy HH:mm")
    private Timestamp createdDate;

    public UserDTO getEvaluatingUsername() {
        return evaluatingUsername;
    }

    public void setEvaluatingUsername(UserDTO evaluatingUsername) {
        this.evaluatingUsername = evaluatingUsername;
    }

    public UserDTO getEvaluatedUsername() {
        return evaluatedUsername;
    }

    public void setEvaluatedUsername(UserDTO evaluatedUsername) {
        this.evaluatedUsername = evaluatedUsername;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
