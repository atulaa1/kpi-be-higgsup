package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.RankingDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiFamePoint;
import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.repository.KpiFamePointRepo;
import com.higgsup.kpi.repository.KpiPointRepo;
import com.higgsup.kpi.repository.KpiUserRepo;
import com.higgsup.kpi.service.RankingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RankingServiceImpl implements RankingService {

    @Autowired
    private KpiPointRepo kpiPointRepo;

    @Autowired
    private KpiUserRepo kpiUserRepo;

    @Autowired
    private KpiFamePointRepo kpiFamePointRepo;

    @Override
    public List<RankingDTO> showNormalPointRanking(Integer currentPage) {
        List<KpiPoint> kpiNormalPointRanking = getNormalPointRanking(currentPage);
        return convertNormalPointEntityToDTO(kpiNormalPointRanking);
    }

    @Override
    public List<RankingDTO> showFamedPointRanking(Integer year, Integer currentPage) {
        List<KpiFamePoint> kpiFamePointRanking = getFamePointRanking(year, currentPage);
        return convertFamePointEntityToDTO(kpiFamePointRanking);
    }

    private List<KpiPoint> getNormalPointRanking(Integer currentPage) {
        Integer rows = rowsReturn(currentPage);
        return kpiPointRepo.getNormalPointRanking(rows);
    }

    private List<KpiFamePoint> getFamePointRanking(Integer year, Integer currentPage) {
        Integer rows = rowsReturn(currentPage);
        return kpiFamePointRepo.getFamePointRanking(year, rows);
    }

    private Integer rowsReturn(Integer currentPage){
        Integer rows = 0;
        switch (currentPage) {
            case 1: {
                rows = 10;
                break;
            }
            case 2: {
                rows = 20;
                break;
            }
            case 3: {
                rows = 50;
                break;
            }
            case 4: {
                rows = 100;
                break;
            }
        }
        return rows;
    }

    private List<RankingDTO> convertNormalPointEntityToDTO(List<KpiPoint> kpiPointRanking) {
        List<RankingDTO> pointRankingDTO = new ArrayList<>();
        for (KpiPoint kpiPoint : kpiPointRanking) {
            RankingDTO rankingDTO = new RankingDTO();
            rankingDTO.setEmployee(convertUserEntityToDTO(kpiPoint.getRatedUser()));
            rankingDTO.setPoint(kpiPoint.getTotalPoint());
            pointRankingDTO.add(rankingDTO);
        }
        return pointRankingDTO;
    }

    private List<RankingDTO> convertFamePointEntityToDTO(List<KpiFamePoint> kpiFamePointRanking) {
        List<RankingDTO> pointRankingDTO = new ArrayList<>();
        for (KpiFamePoint kpiFamePoint : kpiFamePointRanking) {
            RankingDTO rankingDTO = new RankingDTO();
            rankingDTO.setEmployee(convertUserEntityToDTO(kpiFamePoint.getUser()));
            rankingDTO.setPoint(kpiFamePoint.getFamePoint());
            pointRankingDTO.add(rankingDTO);
        }
        return pointRankingDTO;
    }

    private UserDTO convertUserEntityToDTO(KpiUser kpiUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(kpiUser.getUserName());
        userDTO.setFullName(kpiUserRepo.findFullName(kpiUser.getUserName()));
        userDTO.setAvatar(kpiUserRepo.findAvatar(kpiUser.getUserName()));
        return userDTO;
    }
}
