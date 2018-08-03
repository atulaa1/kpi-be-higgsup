package com.higgsup.kpi.services;

import java.util.List;


import com.higgsup.kpi.entity.UserDTO;

public interface UserService{
	
	public List<UserDTO> getUserDetail(String username);
	
}
