package com.higgsup.kpi.service.impl;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.service.UserService;
import com.higgsup.kpi.util.UserAttributesMapper;
import com.higgsup.kpi.util.UtilsLdap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import java.util.List;

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

        List<UserDTO> queryResult = ldapTemplate.search(query, new UserAttributesMapper());
        if (!queryResult.isEmpty()) {
            user = queryResult.get(0);
        }

        return user;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> result = null;
        LdapQuery query = query().where("objectclass").is("user").and("roleForKpi").isPresent();
        result = ldapTemplate.search(query, new UserAttributesMapper());
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
