package com.higgsup.kpi.dto;

import java.sql.Timestamp;

public class GroupDTO<T> extends BaseDTO{
    private Integer id;
    private String name;
    private String description;
    private GroupTypeDTO groupType;
    private Timestamp createdDate;
    private T additionalConfig;

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

    public GroupTypeDTO getGroupType() {
        return groupType;
    }

    public void setGroupType(GroupTypeDTO groupType) {
        this.groupType = groupType;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public T getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(T additionalConfig) {
        this.additionalConfig = additionalConfig;
    }
}
