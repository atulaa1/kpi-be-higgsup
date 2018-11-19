package com.higgsup.kpi.glossary;

public enum SurveyQuestion {
    QUESTION1(1, "Required question 1"),
    QUESTION2(2, "Required question 2"),
    QUESTION3(3, "Required question 3");

    private Integer number;

    private String name;

    SurveyQuestion(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }


}
