package com.higgsup.kpi.service;

import java.util.List;

import com.higgsup.kpi.dto.UserDTO;

public interface LdapUserService{
	
	public UserDTO getUserDetail(String username);
	
	public List<UserDTO> getAllUsers();
	
	public UserDTO updateUserRole(String username, List<String> role);
	
	public List<UserDTO> findUsersByName(String name);
	
}
