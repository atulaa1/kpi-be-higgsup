package com.higgsup.kpi.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.dto.SurveyQuestionManDTO;
import com.higgsup.kpi.entity.KpiSurveyQuestionMan;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.repository.KpiSurveyQuestionManRepo;
import com.higgsup.kpi.service.SurveyQuestionManService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class SurveyQuestionManServiceImpl implements SurveyQuestionManService {

    @Autowired
    private KpiSurveyQuestionManRepo surveyQuestionManRepo;

    public List<SurveyQuestionManDTO> getAllQuestion() {
        List<KpiSurveyQuestionMan> questions = (List<KpiSurveyQuestionMan>) surveyQuestionManRepo.findAll();
        return convertKpiSurveyQuestionManToDTO(questions);
    }

    @Override
    public SurveyQuestionManDTO updateSurveyQuestionOfMan(List<SurveyQuestionManDTO> questions){

        SurveyQuestionManDTO surveyQuestionManBaseDTO = new SurveyQuestionManDTO();

        for(SurveyQuestionManDTO question : questions){
            Integer id = question.getId();

            if (surveyQuestionManRepo.findById(id) == null) {
                surveyQuestionManBaseDTO.setMessage(ErrorCode.NOT_FIND.getDescription());
                surveyQuestionManBaseDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            } else {
                Optional<KpiSurveyQuestionMan> kpiSurveyQuestionManOptional = surveyQuestionManRepo.findById(id);
                if (kpiSurveyQuestionManOptional.isPresent()) {
                    KpiSurveyQuestionMan kpiSurveyQuestionMan = kpiSurveyQuestionManOptional.get();
                    question.setId(kpiSurveyQuestionMan.getId());
                    question.setNumber(kpiSurveyQuestionMan.getNumber());
                    BeanUtils.copyProperties(question, kpiSurveyQuestionMan);
                    surveyQuestionManRepo.save(kpiSurveyQuestionMan);
                }
            }

        }
        return surveyQuestionManBaseDTO;
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
