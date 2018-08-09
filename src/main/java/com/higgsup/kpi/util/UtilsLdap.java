package com.higgsup.kpi.util;

import java.util.List;

import javax.naming.Name;

import org.springframework.ldap.support.LdapNameBuilder;

import com.higgsup.kpi.dto.UserDTO;

public class UtilsLdap {

	public static Name buildDn(UserDTO user, final String BASEDN) {
		return LdapNameBuilder.newInstance(BASEDN).add("cn", "Users").add("cn", user.getFullName()).build();
	}

	public static String convertRole(List<String> listRole) {
		String result = "";
		if (!listRole.isEmpty()) {
			for (String item : listRole) {
				String role = item.replace("ROLE_", "").toLowerCase();
				result += role+",";
			}
		}
		if (result != null && result.length() > 0 && result.charAt(result.length() - 1) == ',') {
			result = result.substring(0, result.length() - 1);
	    }
		return result;
	}
}
