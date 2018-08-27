package com.higgsup.kpi.service.impl;

import java.util.HashSet;
import java.util.Set;

import com.higgsup.kpi.service.LdapUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.higgsup.kpi.dto.UserDTO;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Service("UserDetailsServiceImpl")
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private LdapUserService ldapUserService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDTO user = ldapUserService.getUserDetail(username);
			if(user == null){
				throw new UsernameNotFoundException("Invalid username or password.");
			}
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			for (String role : user.getUserRole()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			}
			return new org.springframework.security.core.userdetails.User(user.getUsername(), "", grantedAuthorities);
		}

	@SuppressWarnings("unused")
	private Set<Object> getAuthority(UserDTO user) {
        HashSet<Object> authorities = new HashSet<>();
		user.getUserRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role));
		});
		return authorities;
	}

}
