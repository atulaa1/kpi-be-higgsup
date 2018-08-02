package com.higgsup.kpi.services;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private LdapTemplate ldapTemplate;

	@Override
	public List<String> getUserRole(String username) {
		List<String> result = new ArrayList<String>();

		LdapQuery query = query().where("objectclass").is("user").and("sAMAccountName").is(username);
		try {
			result = ldapTemplate.search(query, new AttributesMapper<String>() {
				public String mapFromAttributes(Attributes attrs) throws NamingException {
					return (String) attrs.get("roleForKPI").get();
				}
			});
		} catch (NullPointerException ex) {
			
		}
		return result;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

}
