package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.glossary.RoleType;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.util.UserAttributesMapper;
import com.higgsup.kpi.util.UtilsLdap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapUserServiceImpl implements LdapUserService {

	@Autowired
	private Environment env;

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
		} catch (Exception ex) {

		}
		return user;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		List<UserDTO> result = new ArrayList<UserDTO>();
		LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent();
		List<UserDTO> queryResult = ldapTemplate.search(query, new UserAttributesMapper());
		if (!queryResult.isEmpty()) {
			for (UserDTO user : queryResult) {
				for (String role : user.getUserRole()) {
					if (role.equals(RoleType.ADMIN.getName())) {
						user.setUserRole(new ArrayList<String>(Arrays.asList(RoleType.ADMIN.getName())));
						break;
					}
					if (role.equals(RoleType.MAN.getName())) {
						user.setUserRole(new ArrayList<String>(Arrays.asList(RoleType.MAN.getName())));
						break;
					} else {
						user.setUserRole(new ArrayList<String>(Arrays.asList(RoleType.EMPLOYEE.getName())));
					}
				}
				result.add(user);
			}
		}
		return result;
	}

	@Override
	public UserDTO updateUserRole(String username, List<String> listRole) {
		UserDTO user = getUserDetail(username);
		Name dn = UtilsLdap.buildDn(user, env.getProperty("ldap.baseDN"));
		String roleForKpi = null;
		if (!user.getUserRole().containsAll(listRole)) {
			String newRole = UtilsLdap.convertRole(listRole);
			roleForKpi = !listRole.contains(RoleType.EMPLOYEE.getName()) ? "employee," + newRole : newRole;
		}
		Attribute attr = new BasicAttribute("roleForKPI", roleForKpi);
		ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
		ldapTemplate.modifyAttributes(dn, new ModificationItem[] {item});
		return null;
	}

	@Override
	public List<UserDTO> findUsersByName(String name) {
		List<UserDTO> result = null;
		LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent().and("displayName")
				.like("*" + name + "*");
		result = ldapTemplate.search(query, new UserAttributesMapper());
		return result;
	}

}
