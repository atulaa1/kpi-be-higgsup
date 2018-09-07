package com.higgsup.kpi.dto;

public class GroupClubDetail {
    private String host;

    private Integer minNumberOfSessions;

    private Float participationPoint;

    private Float effectivePoint;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getMinNumberOfSessions() {
        return minNumberOfSessions;
    }

    public void setMinNumberOfSessions(Integer minNumberOfSessions) {
        this.minNumberOfSessions = minNumberOfSessions;
    }

    public Float getParticipationPoint() {
        return participationPoint;
    }

    public void setParticipationPoint(Float participationPoint) {
        this.participationPoint = participationPoint;
    }

    public Float getEffectivePoint() {
        return effectivePoint;
    }

    public void setEffectivePoint(Float effectivePoint) {
        this.effectivePoint = effectivePoint;
    }
}
