package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.ProjectDTO;
import com.higgsup.kpi.entity.KpiGroupType;
import com.higgsup.kpi.entity.KpiProject;
import com.higgsup.kpi.repository.KpiProjectRepo;
import com.higgsup.kpi.service.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private KpiProjectRepo kpiProjectRepo;

    @Override
    public List<ProjectDTO> getAllProject() {
        List<KpiProject> kpiProjects = (List<KpiProject>) kpiProjectRepo.findAll();
        List<ProjectDTO> projectDTOS = convertKpiGroupTypeEntityToDTO(kpiProjects);
        return projectDTOS;
    }

    private List<ProjectDTO> convertKpiGroupTypeEntityToDTO(List<KpiProject> kpiProjects) {
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
