package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.SurveyDTO;
import com.higgsup.kpi.entity.KpiSurvey;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.repository.KpiSurveyRepo;
import com.higgsup.kpi.service.SurveyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service

public class SurveyServiceImpl implements SurveyService {

    @Autowired
    private KpiSurveyRepo surveyQuestionManRepo;

    @Override
    public List<SurveyDTO> getAllQuestion() {
        List<KpiSurvey> questions = (List<KpiSurvey>) surveyQuestionManRepo.findAll();
        return convertKpiSurveyQuestionManToDTO(questions);
    }

    @Override
    public SurveyDTO updateSurvey(List<SurveyDTO> questions) {
        SurveyDTO validatedSurveyDTO = new SurveyDTO();
        for (SurveyDTO question : questions) {
            Integer id = question.getId();

            Optional<KpiSurvey> kpiSurveyOptional = surveyQuestionManRepo.findById(id);
            if (kpiSurveyOptional.isPresent()) {
                KpiSurvey kpiSurveyQuestionMan = kpiSurveyOptional.get();

                question.setId(kpiSurveyQuestionMan.getId());
                question.setNumber(kpiSurveyQuestionMan.getNumber());

                BeanUtils.copyProperties(question, kpiSurveyQuestionMan);
                surveyQuestionManRepo.save(kpiSurveyQuestionMan);
            } else {
                validatedSurveyDTO.setMessage(ErrorCode.NOT_FIND.getDescription());
                validatedSurveyDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            }

        }
        return validatedSurveyDTO;
    }

    private List<SurveyDTO> convertKpiSurveyQuestionManToDTO(List<KpiSurvey> questions) {
        List<SurveyDTO> surveyDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(questions)) {
            for (KpiSurvey question : questions) {
                SurveyDTO surveyDTO = new SurveyDTO();
                BeanUtils.copyProperties(question, surveyDTO);
                surveyDTOS.add(surveyDTO);
            }
        }
        return surveyDTOS;
    }
}
