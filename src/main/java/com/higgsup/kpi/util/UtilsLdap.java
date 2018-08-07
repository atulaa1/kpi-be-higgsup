package com.higgsup.kpi.util;

import javax.naming.Name;

import org.springframework.ldap.support.LdapNameBuilder;

import com.higgsup.kpi.model.UserDTO;

public class UtilsLdap {
	
	public static Name buildDn(UserDTO user, final String BASEDN) {
	    return LdapNameBuilder.newInstance(BASEDN)
	      .add("cn", user.getFullName())
	      .add("cn", "Users")
	      .build();
	  }
}
