package com.higgsup.kpi;

import com.higgsup.kpi.entity.KpiUser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dungpx on 10/3/2018.
 */
public class Test {

    public static void main(String[] args) {
        List<KpiUser> userList = new ArrayList<>();
        KpiUser kpiUser = new KpiUser();

        kpiUser.setFirstName("Acd");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Abc");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Aha");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Abd");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Aaa");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Ababba");
        userList.add(kpiUser);

        kpiUser = new KpiUser();
        kpiUser.setFirstName("Arala");
        userList.add(kpiUser);
        System.out.println("------------- Before -----------------");
        System.out.println(userList.size());
        List<KpiUser> usersFilter = userList.stream().filter(user -> user.getFirstName().startsWith("A")).filter(user -> user.getFirstName().startsWith("Ab")).collect(Collectors.toList());
        System.out.println("------------- After -----------------");
        System.out.println(userList.size());
        System.out.println(usersFilter.size());

        List<KpiUser> sortList = userList.stream().sorted((u1,u2) -> u1.getFirstName().compareTo(u2.getFirstName())).collect(Collectors.toList());
        System.out.println(sortList);
    }
}
