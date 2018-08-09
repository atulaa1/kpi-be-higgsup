package com.higgsup.kpi.dto;

import java.util.ArrayList;
import java.util.List;

public class GenericResponseDTO {
    private String message;
    private List<Object> additionalInfos = new ArrayList<>();

    private GenericResponseDTO(String message) {
        this.message = message;
    }

    public static GenericResponseDTO of(String message) {
        return new GenericResponseDTO(message);
    }

    public static GenericResponseDTO success() {
        return of("SUCCESS");
    }

    public static GenericResponseDTO created() {
        return of("CREATED");
    }

    public static GenericResponseDTO updated() {
        return of("UPDATED");
    }

    public static GenericResponseDTO deleted() {
        return of("DELETED");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getAdditionalInfos() {
        return additionalInfos;
    }

    public void setAdditionalInfos(List<Object> additionalInfos) {
        this.additionalInfos = additionalInfos;
    }

    public GenericResponseDTO addAdditionalInfo(Object info) {
        this.additionalInfos.add(info);
        return this;
    }

    public GenericResponseDTO addAdditionalInfo(List<Object> infos) {
        this.additionalInfos.addAll(infos);
        return this;
    }
}
