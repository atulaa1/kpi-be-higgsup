package com.higgsup.kpi;

import com.higgsup.kpi.demo.KPIEventDTO;
import com.higgsup.kpi.demo.KPIEventUserDTO;
import com.higgsup.kpi.demo.KPIUserCustomDTO;
import com.higgsup.kpi.demo.KPIUserDTO;
import com.higgsup.kpi.entity.KpiUser;
import org.springframework.beans.BeanUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dungpx on 10/4/2018.
 */
public class DemoBeanCopy {

    public static void main(String[] args) {


        LocalDateTime localDateTime = LocalDateTime.now();

        KPIEventDTO kpiEventDTO = new KPIEventDTO(1, "Seminar nodejs", "Yeu cau co kinh nghiem js", 1,
                Timestamp.valueOf(localDateTime), Timestamp.valueOf(localDateTime), Timestamp.valueOf(localDateTime),
                Timestamp.valueOf(localDateTime), "test", "test2");


        List<KPIEventUserDTO> kpiEventUserDTOList = new ArrayList<>();
        KPIEventUserDTO kpiEventUserDTO;

        kpiEventUserDTO = new KPIEventUserDTO(2,3);
        kpiEventUserDTOList.add(kpiEventUserDTO);

        kpiEventUserDTO = new KPIEventUserDTO(4,5);
        kpiEventUserDTO.setKpiEvent(kpiEventDTO);
        kpiEventUserDTOList.add(kpiEventUserDTO);

        KPIUserDTO kpiUserDTO = new KPIUserDTO("Bob", "Washtingto", "Who are you", "test@gmail.com");
        kpiUserDTO.setKpiEventUserList(kpiEventUserDTOList);

        KpiUser kpiUser = new KpiUser();

        BeanUtils.copyProperties(kpiUserDTO, kpiUser);
        System.out.println(kpiUser.getFirstName());


        KPIUserCustomDTO userCustomDTO = new KPIUserCustomDTO();
    }
}
