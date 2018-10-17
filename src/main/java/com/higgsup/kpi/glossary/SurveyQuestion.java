package com.higgsup.kpi.glossary;

public enum SurveyQuestion {
    QUESTION1(1, "Question 1"),
    QUESTION2(2, "Question 2"),
    QUESTION3(3, "Question 3"),
    QUESTION4(4, "Question 4"),
    REQUIRED_QUESTIONS(3, "The number of required questions");

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
