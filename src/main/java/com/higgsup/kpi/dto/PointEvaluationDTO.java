package com.higgsup.kpi.dto;

public class PointEvaluationDTO {
    private UserDTO ratedUser;

    private Integer rating;

    public UserDTO getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(UserDTO ratedUser) {
        this.ratedUser = ratedUser;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
