package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.dto.ProjectUserDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.*;
import com.higgsup.kpi.glossary.ErrorCode;
import com.higgsup.kpi.glossary.ErrorMessage;
import com.higgsup.kpi.glossary.ProjectStatus;
import com.higgsup.kpi.repository.KpiProjectRepo;
import com.higgsup.kpi.repository.KpiProjectUserRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.ProjectService;
import com.higgsup.kpi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private KpiProjectUserRepo kpiProjectUserRepo;


    @Override
    public List<ProjectDTO> getAllProject() {
        List<KpiProject> kpiProjects = kpiProjectRepo.findAllFollowCreateDateSorted();
        List<ProjectDTO> projectDTOS = convertKpiProjectEntityToDTO(kpiProjects);
        return projectDTOS;
    }

    public List<ProjectDTO> getProjectsInMonth(){
        List<KpiProject> projectsInMonth = kpiProjectRepo.findAllProjectsInMonth();
        return convertKpiProjectEntityToDTO(projectsInMonth);
    }

    @Override
    public ProjectDTO updateProject(ProjectDTO projectDTO) {

        ProjectDTO validateProjectDTO = new ProjectDTO();
        //check name is null then not update
        if (Objects.nonNull(projectDTO.getName())) {
            Optional<KpiProject> kpiProjectOptional = kpiProjectRepo.findById(projectDTO.getId());
            if (kpiProjectOptional.isPresent()) {
                KpiProject kpiProject = kpiProjectOptional.get();

                //check if same
                KpiProject kpiProjectInDB = kpiProjectRepo.findByName(projectDTO.getName());
                if (!(Objects.nonNull(kpiProjectInDB) && !Objects.equals(projectDTO.getId(), kpiProjectInDB.getId()))) {
                        kpiProject.setName(projectDTO.getName());
                        kpiProject.setActive(projectDTO.getActive());
                        kpiProject.setUpdatedDate(new Timestamp(System.currentTimeMillis()));

                        List<KpiProjectUser> kpiProjectUsers = new ArrayList<>();
                        for (ProjectUserDTO projectUserDTO : projectDTO.getProjectUserList()) {
                            KpiProjectUser kpiProjectUser = new KpiProjectUser();
                            kpiProjectUser.setJoinedDate(new Timestamp(System.currentTimeMillis()));
                            kpiProjectUser.setProjectUser(convertUserDTOToEntity(projectUserDTO.getProjectUser()));
                            kpiProjectUser.setProject(kpiProject);
                            kpiProjectUsers.add(kpiProjectUser);
                        }

                        kpiProject.setProjectUserList(kpiProjectUsers);
                        kpiProjectRepo.save(kpiProject);
                    validateProjectDTO.setId(kpiProject.getId());
                        validateProjectDTO.setName(kpiProject.getName());
                        validateProjectDTO.setActive(kpiProject.getActive());
                        validateProjectDTO.setUpdatedDate(kpiProject.getUpdatedDate());
                    validateProjectDTO.setCreatedDate(kpiProject.getCreatedDate());
                        validateProjectDTO.setProjectUserList(convertListUserEntityToDTO(kpiProjectUsers));
                } else {
                    validateProjectDTO.setErrorCode(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
                    validateProjectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
                }

            } else {
                validateProjectDTO.setErrorCode(ErrorCode.NOT_FIND.getValue());
                validateProjectDTO.setMessage(ErrorMessage.NOT_FIND_PROJECT);
            }
        } else {
            validateProjectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            validateProjectDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
        }

        return validateProjectDTO;
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
    @Transactional
    public ProjectDTO createProject(ProjectDTO projectDTO) {
        ProjectDTO validateProjectDTO = new ProjectDTO();
        projectDTO.setActive(ProjectStatus.ACTIVE.getValue());
        if (Objects.nonNull(projectDTO.getName())) {
            KpiProject kpiProject = kpiProjectRepo.findByName(projectDTO.getName());
            if (Objects.isNull(kpiProject)) {
                if (projectDTO.getProjectUserList().isEmpty()) {
                    validateProjectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
                    validateProjectDTO.setMessage(ErrorMessage.PROJECT_USER_LIST_CAN_NOT_NULL);
                } else {
                    kpiProject = new KpiProject();
                    kpiProject.setName(projectDTO.getName());
                    kpiProject.setActive(projectDTO.getActive());

                    List<KpiProjectUser> kpiProjectUsers = new ArrayList<>();
                    for (ProjectUserDTO projectUserDTO : projectDTO.getProjectUserList()) {
                        KpiProjectUser kpiProjectUser = new KpiProjectUser();
                        kpiProjectUser.setJoinedDate(new Timestamp(System.currentTimeMillis()));
                        kpiProjectUser.setProjectUser(convertUserDTOToEntity(projectUserDTO.getProjectUser()));
                        kpiProjectUser.setProject(kpiProject);
                        kpiProjectUsers.add(kpiProjectUser);
                    }
                    kpiProject.setProjectUserList(kpiProjectUsers);
                    kpiProjectRepo.save(kpiProject);
                    validateProjectDTO.setId(kpiProject.getId());
                    validateProjectDTO.setName(projectDTO.getName());
                    validateProjectDTO.setActive(projectDTO.getActive());
                    validateProjectDTO.setCreatedDate(kpiProject.getCreatedDate());
                    validateProjectDTO.setUpdatedDate(kpiProject.getUpdatedDate());
                    validateProjectDTO.setProjectUserList(convertListUserEntityToDTO(kpiProject.getProjectUserList()));
                }
            } else {
                validateProjectDTO.setErrorCode(ErrorCode.PARAMETERS_ALREADY_EXIST.getValue());
                validateProjectDTO.setMessage(ErrorMessage.PARAMETERS_NAME_PROJECT_EXISTS);
            }
        } else {
            validateProjectDTO.setErrorCode(ErrorCode.NOT_NULL.getValue());
            validateProjectDTO.setMessage(ErrorMessage.NAME_DOES_NOT_ALLOW_NULL);
        }

        return validateProjectDTO;
    }

    private List<ProjectDTO> convertKpiProjectEntityToDTO(List<KpiProject> kpiProjects) {
        List<ProjectDTO> projectDTOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(kpiProjects)) {
            for (KpiProject kpiProject : kpiProjects) {
                ProjectDTO projectDTO = new ProjectDTO();
                projectDTO.setId(kpiProject.getId());
                projectDTO.setName(kpiProject.getName());
                projectDTO.setCreatedDate(kpiProject.getCreatedDate());
                projectDTO.setUpdatedDate(kpiProject.getUpdatedDate());
                projectDTO.setActive(kpiProject.getActive());
                projectDTO.setProjectUserList(convertListUserEntityToDTO(kpiProject.getProjectUserList()));
                projectDTOS.add(projectDTO);
            }
        }
        return projectDTOS;
    }

    private UserDTO convertUserEntityToDTO(KpiUser user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUserName());
        userDTO.setFullName(user.getFullName());
        userDTO.setAvatar(user.getAvatar());
        return userDTO;
    }

    private KpiUser convertUserDTOToEntity(UserDTO userDTO) {
        KpiUser kpiUser = new KpiUser();
        kpiUser.setUserName(userDTO.getUsername());
        String username = kpiUser.getUserName();
        kpiUser.setFullName(kpiUserRepo.findFullName(username));
        kpiUser.setAvatar(kpiUserRepo.findAvatar(username));
        return kpiUser;
    }

    private List<ProjectUserDTO> convertListUserEntityToDTO(List<KpiProjectUser> kpiProjectUsers) {
        List<ProjectUserDTO> projectUserDTOList = new ArrayList<>();
        for (KpiProjectUser kpiProjectUser : kpiProjectUsers) {
            ProjectUserDTO projectUserDTO = new ProjectUserDTO();
            projectUserDTO.setProjectUser(convertUserEntityToDTO(kpiProjectUser.getProjectUser()));
            userService.registerUser(projectUserDTO.getProjectUser().getUsername());
            projectUserDTOList.add(projectUserDTO);
        }
        return projectUserDTOList;
    }


}
