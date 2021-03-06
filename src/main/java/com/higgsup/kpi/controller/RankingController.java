package com.higgsup.kpi.controller;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.RankingDTO;
import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(BaseConfiguration.BASE_API_URL)
public class RankingController {

    @Autowired
    private RankingService rankingService;

    @GetMapping("/ranking/normal-point/year={year}/month={month}/page={currentPage}")
    public Response showNormalPointRanking(@PathVariable("year") Integer year,
                                           @PathVariable("month") Integer month,
                                           @PathVariable("currentPage") Integer currentPage) {
        Response<List<RankingDTO>> response = new Response<>(HttpStatus.OK.value());
        List<RankingDTO> normalPointRanking = rankingService.showNormalPointRanking(year, month, currentPage);
        response.setData(normalPointRanking);
        return response;
    }

    @GetMapping("/ranking/famed-point/year={year}/page={currentPage}")
    public Response showFamedPointRanking(@PathVariable("year")Integer year, @PathVariable("currentPage") Integer currentPage) {
        Response<List<RankingDTO>> response = new Response<>(HttpStatus.OK.value());
        List<RankingDTO> famedPointRanking = rankingService.showFamedPointRanking(year, currentPage);
        response.setData(famedPointRanking);
        return response;
    }
}
