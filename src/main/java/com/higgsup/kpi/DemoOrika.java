package com.higgsup.kpi;

import com.higgsup.kpi.demo.KPIEventDTO;
import com.higgsup.kpi.demo.KPIEventUserDTO;
import com.higgsup.kpi.demo.KPIUserCustomDTO;
import com.higgsup.kpi.demo.KPIUserDTO;
import com.higgsup.kpi.entity.KpiUser;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.MappingStrategy;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import ma.glasnost.orika.metadata.Type;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by dungpx on 10/4/2018.
 */
public class DemoOrika {

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

        // Simple case
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();

        MapperFacade mapper = mapperFactory.getMapperFacade();
        mapper.map(kpiUserDTO, kpiUser);

        System.out.println(kpiUser.getFirstName());

        // specify mapping field
        mapperFactory.classMap(KPIUserDTO.class, KPIUserCustomDTO.class)
                .field("email", "gmail")
                .byDefault()
                .register();
        KPIUserCustomDTO userCustomDTO = new KPIUserCustomDTO();
        MapperFacade mapperCustom = mapperFactory.getMapperFacade();
        mapperCustom.map(kpiUserDTO, userCustomDTO);
        System.out.println(userCustomDTO.getGmail());
    }
}
