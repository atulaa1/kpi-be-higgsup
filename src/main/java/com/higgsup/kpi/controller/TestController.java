package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.EventDTO;
import com.higgsup.kpi.dto.EventTeamBuildingDetail;
import com.higgsup.kpi.service.PointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL + "/test")
public class TestController {

    private final PointService pointService;

    @Autowired
    public TestController(PointService pointService) {
        this.pointService = pointService;
    }

    @GetMapping("/rule-point")
    @PreAuthorize("hasRole('ADMIN')")
    public void calculateRulePoint() {
        pointService.calculateRulePoint();
    }

    @GetMapping("teambuilding-point")
    @PreAuthorize("hasRole('ADMIN')")
    public void calculateTeambuildingPoint(@RequestBody EventDTO<EventTeamBuildingDetail> teamBuildingDTO) {
        pointService.calculateTeambuildingPoint(teamBuildingDTO);
    }
}
