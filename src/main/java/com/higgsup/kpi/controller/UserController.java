package com.higgsup.kpi.controller;


import java.util.*;

import com.higgsup.kpi.dto.Response;
import com.higgsup.kpi.glossary.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	private UserService userService;

	@PreAuthorize("hasAnyRole('EMPLOYEE','ADMIN','MAN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/user-info")
	public Response getUserInfo() {
		Response response = new Response(200);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (Objects.nonNull(username)) {
			UserDTO user = userService.getUserDetail(username);
			response.setData(user);
		} else {
			response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
			response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
		}
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/users")
	public Response getListUsers() {
		Response response = new Response(200);
		List<UserDTO> listUsers = userService.getAllUsers();
		response.setData(listUsers);
		response.setMessage(HttpStatus.OK.getReasonPhrase());
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(BaseConfiguration.BASE_API_URL + "/user-role/{username}")
	public @ResponseBody List<Map<String, Object>> updateUserRole(@RequestBody Map<String, Object> context) {
		String username = (String) context.get("username");
		return null;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/user-role/{username}/{}")
	public @ResponseBody Map<String, Object> getRole(@PathVariable String username) {
		Map<String, Object> result = new HashMap<String, Object>();
		UserDTO user = userService.getUserDetail(username);
		result.put("role", user.getUserRole());
		return result;
	}
	
	
}
