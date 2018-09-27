package com.higgsup.kpi.dto;


public class SeminarSurveyDTO extends BaseDTO {

    private UserDTO evaluatingUsername;

    private UserDTO evaluatedUsername;

    private Integer rating;

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
}
