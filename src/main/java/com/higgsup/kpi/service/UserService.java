package com.higgsup.kpi.service;

import com.higgsup.kpi.entity.KpiUser;

public interface UserService {
	
	public KpiUser getUserDetails(String username);
	
	public KpiUser registUser(String username);
}
