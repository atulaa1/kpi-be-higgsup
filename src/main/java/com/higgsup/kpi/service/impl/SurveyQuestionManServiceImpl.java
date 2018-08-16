package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.SurveyQuestionManDTO;
import com.higgsup.kpi.entity.KpiSurveyQuestionMan;
import com.higgsup.kpi.repository.KpiSurveyQuestionManRepo;
import com.higgsup.kpi.service.SurveyQuestionManService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service

public class SurveyQuestionManServiceImpl implements SurveyQuestionManService {

    @Autowired
    private KpiSurveyQuestionManRepo surveyQuestionManRepo;

    public List<SurveyQuestionManDTO> getAllQuestion() {
        List<KpiSurveyQuestionMan> questions = (List<KpiSurveyQuestionMan>) surveyQuestionManRepo.findAll();
        return convertKpiSurveyQuestionManToDTO(questions);
    }

    private List<SurveyQuestionManDTO> convertKpiSurveyQuestionManToDTO(List<KpiSurveyQuestionMan> questions) {
        List<SurveyQuestionManDTO> surveyQuestionManDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(questions)) {
            for (KpiSurveyQuestionMan question : questions) {
                SurveyQuestionManDTO surveyQuestionManDTO = new SurveyQuestionManDTO();
                BeanUtils.copyProperties(question, surveyQuestionManDTO);
                surveyQuestionManDTOS.add(surveyQuestionManDTO);
            }
        }
        return surveyQuestionManDTOS;
    }
}
