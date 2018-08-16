package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.entity.KpiProject;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.repository.KpiProjectRepo;
import com.higgsup.kpi.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private KpiProjectRepo kpiProjectRepo;

    @Override
    public List<ProjectDTO> getAllProject() {
        List<KpiProject> kpiProjects = (List<KpiProject>) kpiProjectRepo.findAll();
        List<ProjectDTO> projectDTOS = convertKpiProjectEntityToDTO(kpiProjects);
        return projectDTOS;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {
        Optional<KpiProject> kpiProjectOptional = kpiProjectRepo.findById(projectDTO.getId());

        if (kpiProjectOptional.isPresent()) {
            KpiProject kpiProject = kpiProjectOptional.get();
            //check if same
            KpiProject kpiProjectInDB = kpiProjectRepo.findByName(projectDTO.getName());
            if (!(Objects.nonNull(kpiProjectInDB) && !Objects.equals(projectDTO.getId(), kpiProjectInDB.getId()))) {
                BeanUtils.copyProperties(projectDTO, kpiProject, "id", "createdDate");
                kpiProjectRepo.save(kpiProject);
                return new ProjectDTO();
            } else {
                projectDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                projectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
            }

        } else {
            projectDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            projectDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
        }
        return projectDTO;
    }

    @Override
    public ProjectDTO deleteProject(ProjectDTO projectDTO) {
        Optional<KpiProject> kpiProjectOptional = kpiProjectRepo.findById(projectDTO.getId());
        if (kpiProjectOptional.isPresent()) {
            //must check is not used anywhere to be deleted
            //not available
            kpiProjectRepo.deleteById(projectDTO.getId());
            return new ProjectDTO();
        } else {
            projectDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
            projectDTO.setMessage(ErrorMessage.NOT_FIND_GROUP);
            return projectDTO;

        }
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        KpiProject kpiProject = kpiProjectRepo.findByName(projectDTO.getName());
        if (Objects.isNull(kpiProject)) {
            kpiProject = new KpiProject();
            BeanUtils.copyProperties(projectDTO, kpiProject, "id");
            kpiProjectRepo.save(kpiProject);
            return new ProjectDTO();
        } else {
            projectDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
            projectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
        }
        return projectDTO;
    }

    private List<ProjectDTO> convertKpiProjectEntityToDTO(List<KpiProject> kpiProjects) {
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiProjects)) {
            for (KpiProject kpiProject : kpiProjects) {
                ProjectDTO projectDTO = new ProjectDTO();
                BeanUtils.copyProperties(kpiProject, projectDTO);
                projectDTOS.add(projectDTO);
            }
        }
        return projectDTOS;
    }
}
