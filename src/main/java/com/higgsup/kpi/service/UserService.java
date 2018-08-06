package com.higgsup.kpi.service;

import java.util.List;


import com.higgsup.kpi.model.UserDTO;

public interface UserService{
	
	public List<UserDTO> getUserDetail(String username);
	
}
