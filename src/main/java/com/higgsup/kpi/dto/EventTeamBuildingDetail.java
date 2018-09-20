package com.higgsup.kpi.dto;

import java.util.List;

public class EventTeamBuildingDetail {
    private List<EventUserDTO> organizers;
    private List<EventUserDTO> firstPrize;
    private List<EventUserDTO> secondPrize;
    private List<EventUserDTO> thirstPrize;

    public List<EventUserDTO> getOrganizers() {
        return organizers;
    }

    public void setOrganizers(List<EventUserDTO> organizers) {
        this.organizers = organizers;
    }

    public List<EventUserDTO> getFirstPrize() {
        return firstPrize;
    }

    public void setFirstPrize(List<EventUserDTO> firstPrize) {
        this.firstPrize = firstPrize;
    }

    public List<EventUserDTO> getSecondPrize() {
        return secondPrize;
    }

    public void setSecondPrize(List<EventUserDTO> secondPrize) {
        this.secondPrize = secondPrize;
    }

    public List<EventUserDTO> getThirstPrize() {
        return thirstPrize;
    }

    public void setThirstPrize(List<EventUserDTO> thirstPrize) {
        this.thirstPrize = thirstPrize;
    }
}
