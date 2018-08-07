package com.higgsup.kpi.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.model.UserDTO;
import com.higgsup.kpi.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PreAuthorize("hasRole('EMPLOYEE')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/getUserInfo")
	public @ResponseBody Map<String, Object> getUserInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		UserDTO user = userService.getUserDetail(username);
		result.put("username", user.getUsername());
		result.put("fullName", user.getFullName());
		result.put("lastName", user.getLastName());
		result.put("firstName", user.getFirstName());
		result.put("email", user.getEmail());
		result.put("role", user.getUserRole());
		result.put("timestamp", System.currentTimeMillis());
		return result;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/getListUsers")
	public @ResponseBody List<Map<String, Object>> getListUsers() {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		List<UserDTO> listUsers = userService.getAllUsers();
		for(UserDTO user : listUsers) {
			Map<String, Object> userValue = new HashMap<String, Object>();
			userValue.put("fullName", user.getFullName());
			userValue.put("email", user.getEmail());
			userValue.put("username", user.getUsername());
			userValue.put("role", user.getUserRole());
			result.add(userValue);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(BaseConfiguration.BASE_API_URL + "/updateUserRole")
	public @ResponseBody List<Map<String, Object>> updateUserRole(@RequestBody Map<String, Object> context) {
		String username = (String) context.get("username");
		return null;
	}
	
	
}
