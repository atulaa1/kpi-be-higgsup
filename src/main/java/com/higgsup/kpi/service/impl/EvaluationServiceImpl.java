package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.EvaluationDTO;
import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.dto.SurveyDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.service.EvaluationService;
import com.higgsup.kpi.service.ProjectService;
import com.higgsup.kpi.service.SurveyService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private SurveyService surveyService;

    @Override
    public EvaluationDTO getAllEvaluationInfo() {
        List<UserDTO> employee = userService.getAllEmployee();
        List<ProjectDTO> projectsInMonth = projectService.getProjectsInMonth();
        List<SurveyDTO> questions = surveyService.getAllQuestion();

        for(UserDTO e : employee){
            e.setEvaluateQuestion(questions);
        }

        EvaluationDTO evaluationDTO = new EvaluationDTO();
        evaluationDTO.setProjectList(projectsInMonth);
        evaluationDTO.setEmployeeList(employee);

        return evaluationDTO;
    }
}
