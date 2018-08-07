package com.higgsup.kpi.service;

import java.util.List;


import com.higgsup.kpi.model.UserDTO;

public interface UserService{
	
	public UserDTO getUserDetail(String username);
	
	public List<UserDTO> getAllUsers();
	
	public UserDTO updateUserRole(String username, String role);
	
}
