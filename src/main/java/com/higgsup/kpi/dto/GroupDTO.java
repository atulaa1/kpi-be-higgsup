package com.higgsup.kpi.dto;

import java.sql.Timestamp;

public class GroupDTO {
    private Integer id;
    private String name;
    private String description;
    private GroupTypeDTO groupTypeId;
    private Timestamp createdData;
    private String additionalConfig;

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

    public GroupTypeDTO getGroupTypeId() {
        return groupTypeId;
    }

    public void setGroupTypeId(GroupTypeDTO groupTypeId) {
        this.groupTypeId = groupTypeId;
    }

    public Timestamp getCreatedData() {
        return createdData;
    }

    public void setCreatedData(Timestamp createdData) {
        this.createdData = createdData;
    }

    public String getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(String additionalConfig) {
        this.additionalConfig = additionalConfig;
    }
}
