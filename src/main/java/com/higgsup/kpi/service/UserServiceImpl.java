package com.higgsup.kpi.service;

import java.util.ArrayList;
import java.util.List;

import javax.naming.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.util.UserAttributesMapper;
import com.higgsup.kpi.util.UtilsLdap;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserServiceImpl implements UserService {

	@Value("${ldap.baseDN}")
	private String baseDN;

	@Value("${ldap.searchBase}")
	private String searchBase;

	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public UserDTO getUserDetail(String username) {
		UserDTO user = new UserDTO();
		LdapQuery query = query().where("objectclass").is("user").and("sAMAccountName").is(username);
		try {
			List<UserDTO> queryResult = ldapTemplate.search(query, new UserAttributesMapper());
			if (!queryResult.isEmpty()) {
				user = queryResult.get(0);
			}
		} catch (NullPointerException ex) {

		}
		return user;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<UserDTO> result = new ArrayList<UserDTO>();
		LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent();
		try {
			result = ldapTemplate.search(query, new UserAttributesMapper());
		} catch (NullPointerException ex) {

		}
		return result;
	}

	@Override
	public UserDTO updateUserRole(String username, List<String> role) {
		UserDTO user = getUserDetail(username);
		Name dn = UtilsLdap.buildDn(user, baseDN);
		DirContextOperations context = ldapTemplate.lookupContext(dn);
		context.setAttributeValue("roleForKPI", role);
		return null;
	}

}
