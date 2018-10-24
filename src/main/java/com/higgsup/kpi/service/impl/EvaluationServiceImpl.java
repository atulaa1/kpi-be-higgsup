package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.KpiYearMonth;
import com.higgsup.kpi.repository.KpiMonthRepo;
import com.higgsup.kpi.repository.KpiPersonalSurveyRepo;
import com.higgsup.kpi.repository.KpiProjectLogRepo;
import com.higgsup.kpi.service.EvaluationService;
import com.higgsup.kpi.service.ProjectService;
import com.higgsup.kpi.service.SurveyService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.higgsup.kpi.glossary.ErrorCode.MUST_ANSWER_ALL_REQUIRED_QUESTIONS;
import static com.higgsup.kpi.glossary.ManInfo.NUMBER_OF_MAN;
import static com.higgsup.kpi.glossary.SurveyQuestion.QUESTION4;
import static com.higgsup.kpi.glossary.SurveyQuestion.REQUIRED_QUESTIONS;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private KpiPersonalSurveyRepo kpiPersonalSurveyRepo;

    @Autowired
    private KpiProjectLogRepo kpiProjectLogRepo;

    @Autowired
    private KpiMonthRepo kpiMonthRepo;

    @Override
    public EvaluationInfoDTO getAllEvaluationInfo() {
        List<UserDTO> employee = userService.getAllEmployee();
        List<ProjectDTO> projectsInMonth = projectService.getProjectsInMonth();
        List<SurveyDTO> questions = surveyService.getAllQuestion();

        EvaluationInfoDTO evaluationInfoDTO = new EvaluationInfoDTO();
        evaluationInfoDTO.setProjectList(projectsInMonth);
        evaluationInfoDTO.setEmployeeList(employee);
        evaluationInfoDTO.setQuestionList(questions);

        return evaluationInfoDTO;
    }

    @Override
    public EmployeeEvaluationDTO createEmployeeEvaluation(EmployeeEvaluationDTO employeeEvaluationDTO) {

        return employeeEvaluationDTO;
    }

    private List<ErrorDTO> validateEvaluation(EmployeeEvaluationDTO employeeEvaluationDTO) {
        List<ErrorDTO> errors = new ArrayList<>();

        EvaluationInfoDTO evaluationInfoDTO = getAllEvaluationInfo();

        Integer projectQuantity = evaluationInfoDTO.getProjectList().size();

        List<PersonalEvaluationDTO> personalEvaluationDTOList = employeeEvaluationDTO.getPersonalEvaluationDTOList();

        if (personalEvaluationDTOList.isEmpty() || personalEvaluationDTOList.size() < REQUIRED_QUESTIONS.getNumber()) {
            ErrorDTO errorDTO = new ErrorDTO();
            errorDTO.setMessage(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getDescription());
            errorDTO.setErrorCode(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getValue());
            errors.add(errorDTO);
        } else if (personalEvaluationDTOList.size() == REQUIRED_QUESTIONS.getNumber()) {
            for (PersonalEvaluationDTO personalEvaluationDTO : personalEvaluationDTOList) {
                Integer SurveyQuestionNo = personalEvaluationDTO.getSurveyDTO().getNumber();
                if (SurveyQuestionNo.equals(QUESTION4.getNumber())) {
                    ErrorDTO errorDTO = new ErrorDTO();
                    errorDTO.setMessage(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getDescription());
                    errorDTO.setErrorCode(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getValue());
                    errors.add(errorDTO);
                }
            }
        }
        //validate the project evaluation of a last man:
        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();
        if(kpiYearMonthOptional.isPresent()){
            if (kpiProjectLogRepo.countTheNumberOfManEvaluatingProject(kpiYearMonthOptional.get().getId())
                    == (NUMBER_OF_MAN.getValue() - 1)){

            }
        }

        //validate the personal evaluation of a last man:
        if(kpiYearMonthOptional.isPresent()){
            if (kpiPersonalSurveyRepo.countTheNumberOfManEvaluatingEmployee(kpiYearMonthOptional.get().getId())
                    == (NUMBER_OF_MAN.getValue() - 1)){

            }
        }

        return errors;
    }
}
