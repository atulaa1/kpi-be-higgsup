package com.higgsup.kpi.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import com.higgsup.kpi.model.UserDTO;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public List<UserDTO> getUserDetail(String username) {
		List<UserDTO> result = new ArrayList<UserDTO>();
		LdapQuery query = query().where("objectclass").is("user").and("sAMAccountName").is(username);
		try {
			result = ldapTemplate.search(query, new UserAttributesMapper());
		} catch (NullPointerException ex) {
			
		}
		return result;
	}

	// map data ldap to user properties
	private class UserAttributesMapper implements AttributesMapper<UserDTO> {

		public UserDTO mapFromAttributes(Attributes attrs) throws NamingException {
			UserDTO user = new UserDTO();
			user.setUsername(String.valueOf(attrs.get("sAMAccountName").get()));
			String role = String.valueOf(attrs.get("roleForKPI").get());
			if (role != null && !role.isEmpty()) {
				String[] userRoleTmpArr = role.split(",");
				List<String> listUserRole = new ArrayList<String>();
				for(String elm : userRoleTmpArr) {
					listUserRole.add(("ROLE_" + elm).toUpperCase());
				}
				user.setUserRole(listUserRole);
			}
			return user;
		}
		
	}

}
