package com.higgsup.kpi.util;

import com.higgsup.kpi.dto.UserDTO;
import org.springframework.ldap.support.LdapNameBuilder;

import javax.naming.Name;
import java.util.ArrayList;
import java.util.List;

public class UtilsLdap {

    public static Name buildDn(UserDTO user) {
        return LdapNameBuilder.newInstance().add("cn", "Users").add("cn", user.getFullName()).build();
    }

    public static String convertRole(List<String> listRole) {
        List<String> rolesConvert = new ArrayList<>();
        if (!listRole.isEmpty()) {
            for (String item : listRole) {
                String role = item.replace("ROLE_", "").toLowerCase();
                rolesConvert.add(role);
            }
        }
        return String.join(",", rolesConvert);
    }
}
