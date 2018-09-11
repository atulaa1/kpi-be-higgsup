package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class EventUserDTO {
    private UserDTO user;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private EventDTO event;

    private Integer type;

    public UserDTO getUser() {
        return user;
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
