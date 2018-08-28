package com.higgsup.kpi.util;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.ldap.core.AttributesMapper;

import com.higgsup.kpi.dto.UserDTO;

public class UserAttributesMapper implements AttributesMapper<UserDTO> {

    public UserDTO mapFromAttributes(Attributes attrs) throws NamingException {
        UserDTO user = new UserDTO();

        user.setUsername(String.valueOf(attrs.get("sAMAccountName").get()));
        user.setFullName(String.valueOf(attrs.get("displayName").get()));
        user.setLastName(String.valueOf(attrs.get("givenName").get()));
        user.setFirstName(String.valueOf(attrs.get("sn").get()));
        user.setEmail(String.valueOf(attrs.get("userPrincipalName").get()));

        if (attrs.get("roleForKPI").get() != null) {
            String role = String.valueOf(attrs.get("roleForKPI").get());
            if (role != null && !role.isEmpty()) {
                String[] userRoleTmpArr = role.split(",");
                List<String> listUserRole = new ArrayList<String>();

                for (String elm : userRoleTmpArr) {
                    listUserRole.add(("ROLE_" + elm).toUpperCase());
                }
                user.setUserRole(listUserRole);
            }
        }
        return user;
    }

}