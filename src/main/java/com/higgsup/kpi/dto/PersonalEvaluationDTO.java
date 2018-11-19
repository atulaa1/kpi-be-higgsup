package com.higgsup.kpi.dto;

import java.util.List;

public class PersonalEvaluationDTO {
    private UserDTO ratedUser;

    private List<RatedQuestionDTO> ratedQuestionDTO;

    public UserDTO getRatedUser() {
        return ratedUser;
    }

    public void setRatedUser(UserDTO ratedUser) {
        this.ratedUser = ratedUser;
    }

    public List<RatedQuestionDTO> getRatedQuestionDTO() {
        return ratedQuestionDTO;
    }

    public void setRatedQuestionDTO(List<RatedQuestionDTO> ratedQuestionDTO) {
        this.ratedQuestionDTO = ratedQuestionDTO;
    }
}
