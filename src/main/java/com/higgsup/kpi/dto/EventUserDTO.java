package com.higgsup.kpi.dto;

public class EventUserDTO {
    private UserDTO userName;

    private EventDTO event;

    private Integer type;

    public UserDTO getUserName() {
        return userName;
    }

    public void setUserName(UserDTO userName) {
        this.userName = userName;
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
