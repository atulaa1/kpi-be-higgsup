package com.higgsup.kpi.services;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{
	
	public List<String> getUserRole(String username);
	
	
}
