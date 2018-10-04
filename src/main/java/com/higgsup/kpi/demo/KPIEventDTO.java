package com.higgsup.kpi.demo;

import com.higgsup.kpi.entity.KpiGroup;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by dungpx on 10/4/2018.
 */
public class KPIEventDTO {




    private Integer id;

    private String name;

    private String description;

    private Integer status;

    private Timestamp createdDate;

    private Timestamp beginDate;

    private Timestamp endDate;

    private Timestamp updatedDate;

    private String address;

    private List<KPIEventUserDTO> kpiEventUserList;

    private String additionalConfig;


    public KPIEventDTO() {
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @param status
     * @param createdDate
     * @param beginDate
     * @param endDate
     * @param updatedDate
     * @param address
     * @param additionalConfig
     */
    public KPIEventDTO(Integer id, String name, String description, Integer status, Timestamp createdDate, Timestamp beginDate, Timestamp endDate, Timestamp updatedDate, String address, String additionalConfig) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
        this.createdDate = createdDate;
        this.beginDate = beginDate;
        this.endDate = endDate;
        this.updatedDate = updatedDate;
        this.address = address;
        this.additionalConfig = additionalConfig;
    }

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

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
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

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<KPIEventUserDTO> getKpiEventUserList() {
        return kpiEventUserList;
    }

    public void setKpiEventUserList(List<KPIEventUserDTO> kpiEventUserList) {
        this.kpiEventUserList = kpiEventUserList;
    }

    public String getAdditionalConfig() {
        return additionalConfig;
    }

    public void setAdditionalConfig(String additionalConfig) {
        this.additionalConfig = additionalConfig;
    }
}
