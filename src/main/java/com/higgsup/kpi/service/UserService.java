package com.higgsup.kpi.service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiUser;

public interface UserService {
	
	public UserDTO getUserDetails(String username);
	
	public UserDTO registerUser(String username);

	public UserDTO updateInfoUser(String username,UserDTO user);
}
