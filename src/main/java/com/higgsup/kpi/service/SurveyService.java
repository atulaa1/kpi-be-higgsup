package com.higgsup.kpi.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.higgsup.kpi.dto.SurveyDTO;

import java.util.List;

public interface SurveyService {
    List<SurveyDTO> getAllQuestion();

    SurveyDTO updateSurvey(List<SurveyDTO> question) throws JsonProcessingException;
}
