package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.EvaluationService;
import com.higgsup.kpi.service.ProjectService;
import com.higgsup.kpi.service.SurveyService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.higgsup.kpi.glossary.ErrorCode.MUST_ANSWER_ALL_REQUIRED_QUESTIONS;
import static com.higgsup.kpi.glossary.ManInfo.NUMBER_OF_MAN;
import static com.higgsup.kpi.glossary.ProjectStatus.EVALUATED;
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

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private KpiProjectRepo kpiProjectRepo;

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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginUsername = authentication.getPrincipal().toString();

        List<ErrorDTO> validates = validateEvaluation(employeeEvaluationDTO);

        KpiProjectLog kpiProjectLog = new KpiProjectLog();
        KpiPersonalSurvey kpiPersonalSurvey = new KpiPersonalSurvey();

        Optional<KpiYearMonth> kpiYearMonthOptional = kpiMonthRepo.findByMonthCurrent();

        KpiProjectLog finalKpiProjectLog = kpiProjectLog;

        kpiYearMonthOptional.ifPresent(kpiYearMonth -> finalKpiProjectLog.setYearMonth(kpiYearMonth.getId()));

        List<ProjectEvaluationDTO> projectEvaluationDTOS = employeeEvaluationDTO.getProjectEvaluations();
        List<PersonalEvaluationDTO> personalEvaluationDTOS = employeeEvaluationDTO.getPersonalEvaluationDTOList();

        List<ProjectEvaluationDTO> ResponseProjectEvaluationDTOS = new ArrayList<>();

        EmployeeEvaluationDTO validatedEmployeeEvaluationDTO = new EmployeeEvaluationDTO();

        if (CollectionUtils.isEmpty(validates)) {
            for (ProjectEvaluationDTO projectEvaluationDTO : projectEvaluationDTOS) {
                kpiProjectLog.setProject(convertProjectDTOToEntity(projectEvaluationDTO.getProject()));
                kpiProjectLog.setProjectPoint(Float.valueOf(projectEvaluationDTO.getRating()));
                kpiProjectLog.setManUsername(kpiUserRepo.findByUserName(loginUsername));
                List<KpiProjectUser> kpiProjectUserList = kpiProjectLog.getProject().getProjectUserList();

                for(KpiProjectUser kpiProjectUser: kpiProjectUserList){
                    kpiProjectLog.setRatedUsername(kpiProjectUser);
                    kpiProjectLog = kpiProjectLogRepo.save(kpiProjectLog);
                }

                ProjectDTO projectDTO = projectEvaluationDTO.getProject();
                projectDTO.setEvaluation(EVALUATED.getValue());

                List<ProjectUserDTO> projectUserDTOS = projectDTO.getProjectUserList();

                for(ProjectUserDTO projectUserDTO: projectUserDTOS){
                    UserDTO userDTO = projectUserDTO.getProjectUser();
                    userDTO.setEvaluation(1);
                }

                ProjectEvaluationDTO RPProjectEvaluationDTO = new ProjectEvaluationDTO();

                RPProjectEvaluationDTO.setProject(projectDTO);
                ResponseProjectEvaluationDTOS.add(RPProjectEvaluationDTO);

            }
        } else {
            validatedEmployeeEvaluationDTO.setErrorCode(validates.get(0).getErrorCode());
            validatedEmployeeEvaluationDTO.setMessage(validates.get(0).getMessage());
            validatedEmployeeEvaluationDTO.setErrorDTOS(validates);
        }
        BeanUtils.copyProperties(employeeEvaluationDTO, validatedEmployeeEvaluationDTO);

        validatedEmployeeEvaluationDTO.setProjectEvaluations(ResponseProjectEvaluationDTOS);

        return validatedEmployeeEvaluationDTO;
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


    private KpiProject convertProjectDTOToEntity(ProjectDTO projectDTO) {
        KpiProject kpiProject = new KpiProject();

        kpiProject.setId(projectDTO.getId());
        kpiProject.setActive(projectDTO.getActive());
        kpiProject.setCreatedDate(projectDTO.getCreatedDate());
        kpiProject.setUpdatedDate(projectDTO.getUpdatedDate());
        kpiProject.setProjectUserList(convertUserListDTOToEntity(projectDTO.getProjectUserList()));

        return kpiProject;
    }

    private List<KpiProjectUser> convertUserListDTOToEntity(List<ProjectUserDTO> projectUserDTOS) {
        List<KpiProjectUser> kpiProjectUserList = new ArrayList<>();
        for (ProjectUserDTO projectUserDTO : projectUserDTOS) {
            KpiProjectUser kpiProjectUser = new KpiProjectUser();
            kpiProjectUser.setProjectUser(convertUserDTOToEntity(projectUserDTO.getProjectUser()));
            kpiProjectUserList.add(kpiProjectUser);
        }
        return kpiProjectUserList;
    }

    private KpiUser convertUserDTOToEntity(UserDTO userDTO) {
        KpiUser kpiUser = new KpiUser();
        kpiUser.setUserName(userDTO.getUsername());
        String username = kpiUser.getUserName();
        kpiUser.setFullName(kpiUserRepo.findFullName(username));
        kpiUser.setAvatar(kpiUserRepo.findAvatar(username));
        return kpiUser;
    }

    }
