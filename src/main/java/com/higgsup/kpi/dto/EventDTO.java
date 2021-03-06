package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDTO<T> extends BaseDTO {
    private Integer id;

    private String name;

    private String description;

    private Integer status;

    private String address;

    @JsonFormat(pattern = "dd-MM-yyy HH:mm")
    private Timestamp createdDate;

    @JsonFormat(pattern = "dd-MM-yyy HH:mm")
    private Timestamp updatedDate;

    @JsonFormat(pattern = "dd-MM-yyy HH:mm")
    private Timestamp beginDate;

    @JsonFormat(pattern = "dd-MM-yyy HH:mm")
    private Timestamp endDate;

    private GroupDTO group;

    private List<EventUserDTO> eventUserList;

    private T additionalConfig;

    private UserDTO creator;

    private List<SeminarSurveyDTO> listSurveys;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Timestamp getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Timestamp beginDate) {
        this.beginDate = beginDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public GroupDTO getGroup() {
        return group;
    }

    public void setGroup(GroupDTO group) {
        this.group = group;
    }

    public List<EventUserDTO> getEventUserList() {
        return eventUserList;
    }

    public void setEventUserList(List<EventUserDTO> eventUserList) {
        this.eventUserList = eventUserList;
    }

    public T getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(T additionalConfig) {
        this.additionalConfig = additionalConfig;
    }

    public UserDTO getCreator() {
        return creator;
    }

    public void setCreator(UserDTO creator) {
        this.creator = creator;
    }

    public List<SeminarSurveyDTO> getListSurveys() {
        return listSurveys;
    }

    public void setListSurveys(List<SeminarSurveyDTO> listSurveys) {
        this.listSurveys = listSurveys;
    }
}
