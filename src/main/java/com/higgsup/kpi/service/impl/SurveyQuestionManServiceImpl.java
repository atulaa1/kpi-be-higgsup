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
    public SurveyQuestionManDTO updateSurveyQuestionOfMan(SurveyQuestionManDTO surveyQuestionManDTO){
        Integer id = surveyQuestionManDTO.getId();
        SurveyQuestionManDTO surveyQuestionManDTO1 = new SurveyQuestionManDTO();

        if (surveyQuestionManRepo.findById(id) == null) {
            surveyQuestionManDTO1.setMessage(ErrorCode.NOT_FIND.getDescription());
            surveyQuestionManDTO1.setErrorCode(ErrorCode.NOT_FIND.getValue());
        } else {
            Optional<KpiSurveyQuestionMan> kpiSurveyQuestionManOptional = surveyQuestionManRepo.findById(id);
            if (kpiSurveyQuestionManOptional.isPresent()) {
                KpiSurveyQuestionMan kpiSurveyQuestionMan = kpiSurveyQuestionManOptional.get();
                surveyQuestionManDTO.setId(kpiSurveyQuestionMan.getId());
                surveyQuestionManDTO.setNumber(kpiSurveyQuestionMan.getNumber());
                BeanUtils.copyProperties(surveyQuestionManDTO, kpiSurveyQuestionMan);
                surveyQuestionManRepo.save(kpiSurveyQuestionMan);
            }
        }

        return surveyQuestionManDTO1;
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
