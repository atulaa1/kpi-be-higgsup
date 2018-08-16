package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.SurveyQuestionManDTO;

import java.util.List;

public interface SurveyQuestionManService {
    List<SurveyQuestionManDTO> getAllQuestion();
    SurveyQuestionManDTO updateSurveyQuestionOfMan(List<SurveyQuestionManDTO> surveyQuestionManDTOs) throws JsonProcessingException;
}
