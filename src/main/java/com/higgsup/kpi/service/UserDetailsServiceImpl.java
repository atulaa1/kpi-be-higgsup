package com.higgsup.kpi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.higgsup.kpi.entity.UserDTO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<UserDTO> listUsers = userService.getUserDetail(username);
		if (!listUsers.isEmpty()) {
			UserDTO user = listUsers.get(0);
			if(user == null){
				throw new UsernameNotFoundException("Invalid username or password.");
			}
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			for (String role : user.getUserRole()) {
				grantedAuthorities.add(new SimpleGrantedAuthority(role));
			}
			return new org.springframework.security.core.userdetails.User(user.getUsername(), null, grantedAuthorities);
		}
		return null;
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
