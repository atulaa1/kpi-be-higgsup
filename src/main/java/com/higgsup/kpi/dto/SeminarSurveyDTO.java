package com.higgsup.kpi.dto;

import java.util.Map;

public class SeminarSurveyDTO extends BaseDTO {

    private EventUserDTO evaluatingUsername;

    private Integer rating;

    private EventDTO event;

    private Map<EventUserDTO, Integer> hostEvaluation;

    public EventUserDTO getEvaluatingUsername() {
        return evaluatingUsername;
    }

    public void setEvaluatingUsername(EventUserDTO evaluatingUsername) {
        this.evaluatingUsername = evaluatingUsername;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public EventDTO getEvent() {
        return event;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    public Map<EventUserDTO, Integer> getHostEvaluation() {
        return hostEvaluation;
    }

    public void setHostEvaluation(Map<EventUserDTO, Integer> hostEvaluation) {
        this.hostEvaluation = hostEvaluation;
    }
}
