package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.*;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.EvaluatingStatus;
import com.higgsup.kpi.glossary.SurveyQuestion;
import com.higgsup.kpi.repository.*;
import com.higgsup.kpi.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.higgsup.kpi.glossary.ErrorCode.*;
import static com.higgsup.kpi.glossary.ManInfo.NUMBER_OF_MAN;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;

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
    private KpiEvaluationRepo kpiEvaluationRepo;

    @Autowired
    private KpiSurveyRepo kpiSurveyRepo;

    @Override
    public EvaluationInfoDTO getAllEvaluationInfo() {
        List<UserDTO> employee = userService.getAllEmployee();
        List<ProjectDTO> projectsInMonth = projectService.getProjectsInMonth();
        List<SurveyDTO> questions = surveyService.getAllQuestion();

        Optional<KpiYearMonth> kpiYearMonth;
        LocalDate currentDate = LocalDate.now();
        LocalDate endDateOfMonth = currentDate.with(lastDayOfMonth());
        if(currentDate.getDayOfMonth() == endDateOfMonth.getDayOfMonth()){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        }

        List<String> evaluated = kpiPersonalSurveyRepo.evaluatedList(kpiYearMonth.get().getId());
        for(UserDTO userDTO : employee){
            if(evaluated.contains(userDTO.getUsername())){
                userDTO.setEvaluateStatus(EvaluatingStatus.FINISH.getValue());
            }else{
                userDTO.setEvaluateStatus(EvaluatingStatus.UNFINISHED.getValue());
            }
        }

        List<Integer> projectEvaluated = kpiProjectLogRepo.getAllProjectEvaluated(kpiYearMonth.get().getId());
        for(ProjectDTO projectDTO : projectsInMonth){
            if(projectEvaluated.contains(projectDTO.getId())){
                projectDTO.setEvaluateStatus(EvaluatingStatus.FINISH.getValue());
            }else{
                projectDTO.setEvaluateStatus(EvaluatingStatus.UNFINISHED.getValue());
            }
        }

        EvaluationInfoDTO evaluationInfoDTO = new EvaluationInfoDTO();
        evaluationInfoDTO.setProjectList(projectsInMonth);
        evaluationInfoDTO.setEmployeeList(employee);
        evaluationInfoDTO.setQuestionList(questions);
        return evaluationInfoDTO;
    }

    @Override
    public EvaluationDTO createEvaluation(EvaluationDTO kpiEvaluationDTO) {
        Optional<KpiYearMonth> kpiYearMonth;
        LocalDate currentDate = LocalDate.now();
        LocalDate endDateOfMonth = currentDate.with(lastDayOfMonth());
        if(currentDate.getDayOfMonth() == endDateOfMonth.getDayOfMonth()){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        }

        List<ErrorDTO> validateEvaluation = validateEvaluation(kpiEvaluationDTO);
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        if(CollectionUtils.isEmpty(validateEvaluation)){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String loginUsername = authentication.getPrincipal().toString();
            KpiUser evaluator = kpiUserRepo.findByUserName(loginUsername);

            BeanUtils.copyProperties(kpiEvaluationDTO, evaluationDTO);
            evaluationDTO.setManUser(convertUserEntityToDTO(evaluator));

            KpiEvaluation kpiEvaluation = convertEvaluationDTOToEntity(kpiEvaluationDTO);
            kpiEvaluation.setManUser(evaluator);
            kpiEvaluation.setYearMonthId(kpiYearMonth.get().getId());
            kpiEvaluationRepo.save(kpiEvaluation);

            List<ProjectEvaluationDTO> projectEvaluationDTOS = kpiEvaluationDTO.getProjectEvaluations();
            List<PersonalEvaluationDTO> personalEvaluationDTOS = kpiEvaluationDTO.getPersonalEvaluations();
            List<KpiSurvey> questions = kpiSurveyRepo.findQuestion();

            for (PersonalEvaluationDTO personalEvaluationDTO : personalEvaluationDTOS) {
                for (RatedQuestionDTO ratedQuestionDTO : personalEvaluationDTO.getRatedQuestionDTO()) {
                    KpiPersonalSurvey kpiPersonalSurvey = new KpiPersonalSurvey();
                    kpiPersonalSurvey.setRatedUsername(convertUserDTOToEntity(personalEvaluationDTO.getRatedUser()));
                    kpiPersonalSurvey.setPersonalPoint(ratedQuestionDTO.getPoint());
                    kpiPersonalSurvey.setEvaluation(kpiEvaluation);

                    Optional<KpiSurvey> question = questions.stream()
                            .filter(q -> q.getNumber().equals(ratedQuestionDTO.getSurveyDTO().getNumber()))
                            .findFirst();
                    kpiPersonalSurvey.setQuestion(question.get());
                    kpiPersonalSurveyRepo.save(kpiPersonalSurvey);
                }
            }

            for (ProjectEvaluationDTO projectEvaluationDTO : projectEvaluationDTOS) {
                KpiProjectLog kpiProjectLog = new KpiProjectLog();
                kpiProjectLog.setProject(convertProjectDTOToEntity(projectEvaluationDTO.getProject()));
                kpiProjectLog.setProjectPoint(projectEvaluationDTO.getPoint());
                kpiProjectLog.setEvaluation(kpiEvaluation);
                kpiProjectLogRepo.save(kpiProjectLog);
            }

        }else{
            evaluationDTO.setErrorCode(validateEvaluation.get(0).getErrorCode());
            evaluationDTO.setMessage(validateEvaluation.get(0).getMessage());
            evaluationDTO.setErrorDTOS(validateEvaluation);
        }
        return evaluationDTO;
    }

    @Override
    public List<EvaluationDTO> getAllEvaluation(){
        List<KpiEvaluation> listEvaluation = kpiEvaluationRepo.findAllEvaluation();
        List<EvaluationDTO> evaluationDTOS = new ArrayList<>();

        for(KpiEvaluation kpiEvaluation : listEvaluation){
            EvaluationDTO evaluationDTO = convertEvaluationEntityToDTO(kpiEvaluation);
            List<PersonalEvaluationDTO> personalEvaluationDTOs = new ArrayList<>();
            List<ProjectEvaluationDTO> projectEvaluationDTOs = new ArrayList<>();

            List<String> ratedUsername = kpiPersonalSurveyRepo.ratedUsername(kpiEvaluation.getId());
            for(String username : ratedUsername){
                List<KpiPersonalSurvey> kpiPersonalSurveys = kpiPersonalSurveyRepo.findByUsername(username, kpiEvaluation.getId());
                PersonalEvaluationDTO personalEvaluationDTO = new PersonalEvaluationDTO();
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(username);
                personalEvaluationDTO.setRatedUser(userDTO);
                List<RatedQuestionDTO> ratedQuestionDTOs = new ArrayList<>();
                for(KpiPersonalSurvey kpiPersonalSurvey : kpiPersonalSurveys){
                    RatedQuestionDTO ratedQuestionDTO = new RatedQuestionDTO();
                    SurveyDTO surveyDTO = convertSurveyEntityToDTO(kpiPersonalSurvey.getQuestion());
                    ratedQuestionDTO.setPoint(kpiPersonalSurvey.getPersonalPoint());
                    ratedQuestionDTO.setSurveyDTO(surveyDTO);
                    ratedQuestionDTOs.add(ratedQuestionDTO);
                }
                personalEvaluationDTO.setRatedQuestionDTO(ratedQuestionDTOs);
                personalEvaluationDTOs.add(personalEvaluationDTO);
            }

            List<KpiProjectLog> kpiProjectLogs = kpiProjectLogRepo.findByEvaluation(kpiEvaluation);
            for(KpiProjectLog kpiProjectLog : kpiProjectLogs){
                ProjectEvaluationDTO projectEvaluationDTO = new ProjectEvaluationDTO();
                projectEvaluationDTO.setPoint(kpiProjectLog.getProjectPoint());
                ProjectDTO projectDTO = convertProjectEntityToDTO(kpiProjectLog.getProject());
                projectEvaluationDTO.setProject(projectDTO);
                projectEvaluationDTOs.add(projectEvaluationDTO);
            }

            evaluationDTO.setPersonalEvaluations(personalEvaluationDTOs);
            evaluationDTO.setProjectEvaluations(projectEvaluationDTOs);
            evaluationDTOS.add(evaluationDTO);
        }
        return evaluationDTOS;
    }

    private List<ErrorDTO> validateEvaluation(EvaluationDTO evaluationDTO) {
        List<ErrorDTO> errors = new ArrayList<>();
        Optional<KpiYearMonth> kpiYearMonth;
        LocalDate currentDate = LocalDate.now();
        LocalDate endDateOfMonth = currentDate.with(lastDayOfMonth());
        if(currentDate.getDayOfMonth() == endDateOfMonth.getDayOfMonth()){
            kpiYearMonth = kpiMonthRepo.findByMonthCurrent();
        }else{
            kpiYearMonth = kpiMonthRepo.findByPreviousMonth();
        }

        List<PersonalEvaluationDTO> personalEvaluationDTOList = evaluationDTO.getPersonalEvaluations();
        for (PersonalEvaluationDTO personalEvaluationDTO : personalEvaluationDTOList) {
            List<RatedQuestionDTO> ratedQuestionDTO = personalEvaluationDTO.getRatedQuestionDTO();
            List<Integer> numberQuestion = ratedQuestionDTO.stream()
                    .map(r -> r.getSurveyDTO().getNumber())
                    .collect(Collectors.toList());
            if(!numberQuestion.contains(SurveyQuestion.QUESTION1.getNumber()) ||
                    !numberQuestion.contains(SurveyQuestion.QUESTION2.getNumber()) ||
                    !numberQuestion.contains(SurveyQuestion.QUESTION3.getNumber())){
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setMessage(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getDescription() + " of " + personalEvaluationDTO.getRatedUser().getUsername());
                errorDTO.setErrorCode(MUST_ANSWER_ALL_REQUIRED_QUESTIONS.getValue());
                errors.add(errorDTO);
            }
        }

        if(kpiEvaluationRepo.numberOfManCompleteEvaluation(kpiYearMonth.get().getId()).equals(NUMBER_OF_MAN.getValue() - 1)){
            EvaluationInfoDTO evaluationInfoDTO = getAllEvaluationInfo();
            List<Integer> projectNotEvaluate = evaluationInfoDTO.getProjectList()
                    .stream()
                    .filter(p -> p.getEvaluateStatus().equals(EvaluatingStatus.UNFINISHED.getValue()))
                    .map(ProjectDTO::getId)
                    .collect(Collectors.toList());
            List<String> userNotEvaluate = evaluationInfoDTO.getEmployeeList()
                    .stream()
                    .filter(u -> u.getEvaluateStatus().equals(EvaluatingStatus.UNFINISHED.getValue()))
                    .map(UserDTO::getUsername)
                    .collect(Collectors.toList());

            List<Integer> projectEvaluationList = evaluationDTO.getProjectEvaluations()
                    .stream()
                    .map(p -> p.getProject().getId())
                    .collect(Collectors.toList());
            List<String> userEvaluationList = evaluationDTO.getPersonalEvaluations()
                    .stream()
                    .map(u -> u.getRatedUser().getUsername())
                    .collect(Collectors.toList());

            if(!projectEvaluationList.containsAll(projectNotEvaluate)){
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setMessage(MUST_EVALUATE_REMAINING_PROJECTS.getDescription());
                errorDTO.setErrorCode(MUST_EVALUATE_REMAINING_PROJECTS.getValue());
                errors.add(errorDTO);
            }

            if(!userEvaluationList.containsAll(userNotEvaluate)){
                ErrorDTO errorDTO = new ErrorDTO();
                errorDTO.setMessage(MUST_EVALUATE_REMAINING_EMPLOYEES.getDescription());
                errorDTO.setErrorCode(MUST_EVALUATE_REMAINING_EMPLOYEES.getValue());
                errors.add(errorDTO);
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
        return kpiProject;
    }

    private KpiUser convertUserDTOToEntity(UserDTO userDTO) {
        KpiUser kpiUser = new KpiUser();
        kpiUser.setUserName(userDTO.getUsername());
        String username = kpiUser.getUserName();
        kpiUser.setFullName(kpiUserRepo.findFullName(username));
        kpiUser.setAvatar(kpiUserRepo.findAvatar(username));
        return kpiUser;
    }

    private KpiEvaluation convertEvaluationDTOToEntity(EvaluationDTO evaluationDTO){
        KpiEvaluation kpiEvaluation = new KpiEvaluation();
        BeanUtils.copyProperties(evaluationDTO, kpiEvaluation);
        return kpiEvaluation;
    }

    private UserDTO convertUserEntityToDTO(KpiUser kpiUser){
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(kpiUser, userDTO);
        userDTO.setUsername(kpiUser.getUserName());
        return userDTO;
    }

    private EvaluationDTO convertEvaluationEntityToDTO(KpiEvaluation kpiEvaluation){
        EvaluationDTO evaluationDTO = new EvaluationDTO();
        BeanUtils.copyProperties(kpiEvaluation, evaluationDTO);

        UserDTO userDTO = convertUserEntityToDTO(kpiEvaluation.getManUser());
        evaluationDTO.setManUser(userDTO);

        Optional<KpiYearMonth> yearMonth = kpiMonthRepo.findById(kpiEvaluation.getYearMonthId());
        YearMonthDTO yearMonthDTO = new YearMonthDTO();
        yearMonthDTO.setYearMonth(yearMonth.get().getYearMonth());
        evaluationDTO.setYearMonth(yearMonthDTO);

        return evaluationDTO;
    }

    private ProjectDTO convertProjectEntityToDTO(KpiProject project){
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        return projectDTO;
    }

    private SurveyDTO convertSurveyEntityToDTO(KpiSurvey kpiSurvey){
        SurveyDTO surveyDTO = new SurveyDTO();
        surveyDTO.setNumber(kpiSurvey.getNumber());
        surveyDTO.setQuestion(kpiSurvey.getQuestion());
        return surveyDTO;
    }
}
