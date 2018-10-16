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
    public List<RankingDTO> showNormalPointRanking() {
        List<KpiPoint> kpiNomalPointRanking = kpiPointRepo.getRanking();
        List<RankingDTO> nomalPointRankingDTO = convertPointEntityToDTO(kpiNomalPointRanking);

        return nomalPointRankingDTO;
    }

    private List<RankingDTO> convertPointEntityToDTO(List<KpiPoint> kpiNomalPointRanking) {
        List<RankingDTO> nomalPointRankingDTO = new ArrayList<>();
        for (KpiPoint kpiPoint : kpiNomalPointRanking) {
            RankingDTO rankingDTO = new RankingDTO();
            rankingDTO.setEmployee(convertUserEntityToDTO(kpiPoint.getRatedUser()));
            rankingDTO.setTotalPoint(kpiPoint.getTotalPoint());
            nomalPointRankingDTO.add(rankingDTO);
        }
        return nomalPointRankingDTO;
    }

    private UserDTO convertUserEntityToDTO(KpiUser kpiUser) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(kpiUser.getUserName());
        userDTO.setFullName(kpiUserRepo.findFullName(kpiUser.getUserName()));
        return userDTO;
    }

    @Override
    public List<PointDTO> showFamedPointRanking() {
        return null;
    }
}
