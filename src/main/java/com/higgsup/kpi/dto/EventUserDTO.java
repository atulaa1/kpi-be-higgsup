package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class EventUserDTO {
    private UserDTO user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EventDTO event;

    private Integer type;

    private Integer status;

    List<SeminarSurveyDTO> seminarSurveys;

    public UserDTO getUser() {
        return user;
    }

    public Integer getStatus() {
        return status;
    }

    public List<SeminarSurveyDTO> getSeminarSurveys() {
        return seminarSurveys;
    }

    public void setSeminarSurveys(List<SeminarSurveyDTO> seminarSurveys) {
        this.seminarSurveys = seminarSurveys;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
