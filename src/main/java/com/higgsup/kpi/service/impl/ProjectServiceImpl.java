package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.EventUserDTO;
import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.dto.ProjectUserDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.glossary.ProjectStatus;
import com.higgsup.kpi.repository.KpiProjectRepo;
import com.higgsup.kpi.service.ProjectService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private KpiProjectRepo kpiProjectRepo;

    @Autowired
    private UserService userService;

    @Override
    public List<ProjectDTO> getAllProject() {
        List<KpiProject> kpiProjects = kpiProjectRepo.findAllFollowCreateDateSorted();
        List<ProjectDTO> projectDTOS = convertKpiProjectEntityToDTO(kpiProjects);
        return projectDTOS;
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {

        //check name is null then not update
        if (Objects.nonNull(projectDTO.getName())) {
            Optional<KpiProject> kpiProjectOptional = kpiProjectRepo.findById(projectDTO.getId());
            if (kpiProjectOptional.isPresent()) {
                KpiProject kpiProject = kpiProjectOptional.get();

                //check if same
                KpiProject kpiProjectInDB = kpiProjectRepo.findByName(projectDTO.getName());
                if (!(Objects.nonNull(kpiProjectInDB) && !Objects.equals(projectDTO.getId(), kpiProjectInDB.getId()))) {
                    BeanUtils.copyProperties(projectDTO, kpiProject, "id", "createdDate");

                    kpiProject.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

                    kpiProject = kpiProjectRepo.save(kpiProject);

                    BeanUtils.copyProperties(kpiProject, projectDTO);
                } else {
                    projectDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    projectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
                }

            } else {
                projectDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                projectDTO.setMessage(ErrorMessage.NOT_FIND_PROJECT);
            }
        } else {
            projectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            projectDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
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
            projectDTO.setMessage(ErrorMessage.NOT_FIND_PROJECT);
            return projectDTO;

        }
    }

    @Override
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        projectDTO.setActive(ProjectStatus.ACTIVE.getValue());
        if (Objects.nonNull(projectDTO.getName())) {
            KpiProject kpiProject = kpiProjectRepo.findByName(projectDTO.getName());
            if (Objects.isNull(kpiProject)) {

                kpiProject = new KpiProject();
                BeanUtils.copyProperties(projectDTO, kpiProject, "id");
                kpiProject = kpiProjectRepo.save(kpiProject);
                BeanUtils.copyProperties(kpiProject, projectDTO);

//                if (projectDTO.getProjectUserList().isEmpty()) {
//                    projectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
//                    projectDTO.setMessage(ErrorMessage.PROJECT_USER_LIST_CAN_NOT_NULL);
//                } else {
//                    kpiProject = new KpiProject();
//                    BeanUtils.copyProperties(projectDTO, kpiProject, "id");
//                    kpiProject = kpiProjectRepo.save(kpiProject);
//                    BeanUtils.copyProperties(kpiProject, projectDTO);
//                }
            } else {
                projectDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
                projectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
            }
        } else {
            projectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            projectDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
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

    private List<KpiProjectUser> convertProjectUsersToEntity(KpiProject kpiProject,
            List<ProjectUserDTO> projectUserList) {
        List<KpiProjectUser> kpiProjectUsers = new ArrayList<>();

        for (ProjectUserDTO projectUserDTO : projectUserList) {
            KpiProjectUser kpiProjectUser = new KpiProjectUser();
            userService.registerUser(projectUserDTO.getProjectUser().getUsername());

            kpiProjectUser.setId(kpiProject.getId());
            //kpiEventUser.setKpiEventUserPK(kpiEventUserPK);

            kpiProjectUsers.add(kpiProjectUser);

        }
        return kpiProjectUsers;
    }
}
