package com.higgsup.kpi.model;

import java.util.List;

public class UserDTO {
	
	private String username;
	
	private String password;
	
	private List<String> userRole;

	public UserDTO() {
	}

	public UserDTO(String username, String password, List<String> userRole) {
		this.username = username;
		this.password = password;
		this.userRole = userRole;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<String> getUserRole() {
		return userRole;
	}

	public void setUserRole(List<String> userRole) {
		this.userRole = userRole;
	}

}
