package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.PointDTO;
import com.higgsup.kpi.dto.RankingDTO;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiPoint;
import com.higgsup.kpi.entity.KpiUser;
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
    public List<RankingDTO> showNormalPointRanking(Integer curentPage) {
        List<KpiPoint> kpiNomalPointRanking = getListPointForRanking(curentPage);
        List<RankingDTO> nomalPointRankingDTO = convertPointEntityToDTO(kpiNomalPointRanking);

        return nomalPointRankingDTO;
    }

    private List<KpiPoint> getListPointForRanking(Integer curentPage) {
        List<KpiPoint> kpiPoints;
        Integer offset = 0;
        Integer rows = 0;
        switch (curentPage) {
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
        kpiPoints = kpiPointRepo.getRanking(offset, rows);
        return kpiPoints;
    }

    @Override
    public List<PointDTO> showFamedPointRanking() {
        return null;
    }

    private List<RankingDTO> convertPointEntityToDTO(List<KpiPoint> kpiNomalPointRanking) {
        List<RankingDTO> nomalPointRankingDTO = new ArrayList<>();
        for (int curentRank = 0; curentRank < kpiNomalPointRanking.size(); curentRank++) {
            RankingDTO rankingDTO = new RankingDTO();
            KpiUser employee = kpiNomalPointRanking.get(curentRank).getRatedUser();
            Float totalNormalPoint = kpiNomalPointRanking.get(curentRank).getTotalPoint();
            rankingDTO.setEmployee(convertUserEntityToDTO(employee));
            rankingDTO.setTotalPoint(totalNormalPoint);

            nomalPointRankingDTO.add(rankingDTO);
        }
        return nomalPointRankingDTO;
    }

    private UserDTO convertUserEntityToDTO(KpiUser kpiUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(kpiUser.getUserName());
        userDTO.setFullName(kpiUserRepo.findFullName(kpiUser.getUserName()));
        userDTO.setAvatar(kpiUserRepo.findAvatar(kpiUser.getUserName()));
        return userDTO;
    }
}
