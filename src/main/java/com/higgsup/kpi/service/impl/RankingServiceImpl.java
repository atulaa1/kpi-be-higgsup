package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.BestPerformancesDTO;
import com.higgsup.kpi.dto.PointDTO;
import com.higgsup.kpi.dto.RankingDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.glossary.RankingType;
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

    @Override
    public List<RankingDTO> showNormalPointRanking(Integer currentPage) {
        List<KpiPoint> kpiPointRanking = getListPointForRanking(RankingType.NORMAL_POINT_RANKING, currentPage);
        List<RankingDTO> pointRankingDTO = convertPointEntityToDTO(kpiPointRanking);

        return pointRankingDTO;
    }

    @Override
    public List<RankingDTO> showFamedPointRanking(Integer currentPage) {
        List<KpiPoint> kpiPointRanking = getListPointForRanking(RankingType.FAMED_POINT_RANKING, currentPage);
        List<RankingDTO> pointRankingDTO = convertPointEntityToDTO(kpiPointRanking);

        return pointRankingDTO;
    }

    @Override
    public List<BestPerformancesDTO> showBestPerformancesOfEachMonth() {
        return null;
    }

    private List<KpiPoint> getListPointForRanking(RankingType rankingType, Integer currentPage) {
        List<KpiPoint> kpiPoints;
        Integer offset = 0;
        Integer rows = 0;
        switch (currentPage) {
            case 1: {
                rows = 10;
                break;
            }
            case 2: {
                offset = 10;
                rows = 10;
                break;
            }
            case 3: {
                offset = 20;
                rows = 30;
                break;
            }
            case 4: {
                offset = 50;
                rows = 50;
                break;
            }
        }

        if (rankingType == RankingType.NORMAL_POINT_RANKING) {
            kpiPoints = kpiPointRepo.getNomalPointRanking(offset, rows);
        } else {
            kpiPoints = kpiPointRepo.getFamedPointRanking(offset, rows);
        }
        return kpiPoints;
    }

    private List<RankingDTO> convertPointEntityToDTO(List<KpiPoint> kpiPointRanking) {
        List<RankingDTO> pointRankingDTO = new ArrayList<>();
        for (KpiPoint kpiPoint : kpiPointRanking) {
            RankingDTO rankingDTO = new RankingDTO();
            rankingDTO.setEmployee(convertUserEntityToDTO(kpiPoint.getRatedUser()));
            rankingDTO.setTotalPoint(kpiPoint.getTotalPoint());
            rankingDTO.setFamedPoint(kpiPoint.getFamedPoint());
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
