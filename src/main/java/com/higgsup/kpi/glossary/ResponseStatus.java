package com.higgsup.kpi.glossary;

/**
 * Created by tiepnm on 5/17/2018.
 */
public enum ResponseStatus {
    SUCCESS("success"),  FALSE("false");

    private String value;

    ResponseStatus(String value) {
        this.value = value;

    }

    public String getValue() {
        return value;
    }
}
