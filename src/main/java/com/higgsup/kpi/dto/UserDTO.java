package com.higgsup.kpi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class UserDTO {
	
	private String username;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password;
	
	private String lastName;
	
	private String firstName;
	
	private String fullName;
	
	private String email;
	
	private List<String> userRole;

	public UserDTO() {
	}

	public UserDTO(String username, String password, String lastName, String firstName, String fullName, String email,
			List<String> userRole) {
		this.username = username;
		this.password = password;
		this.lastName = lastName;
		this.firstName = firstName;
		this.fullName = fullName;
		this.email = email;
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

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	

}
