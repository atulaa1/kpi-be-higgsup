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
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.util.UtilsValidate;

@RestController
public class UserController {

	@Autowired
	private LdapUserService ldapUserService;

	@PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN', 'MAN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/user-info")
	public @ResponseBody Response getUserInfo() {
		Response response = new Response(200);
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		if (username != null && !username.equals("")) {
			if (!UtilsValidate.containRegex(username)) {
				UserDTO user = ldapUserService.getUserDetail(username);
				if (user != null) {
					response.setData(user);
				} else {
					response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
					response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
				}
			}
		}
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/users")
	public @ResponseBody Response getListUsers() {
		Response response = new Response(200);
		List<UserDTO> listUsers = ldapUserService.getAllUsers();
		if (!listUsers.isEmpty()) {
			response.setData(listUsers);
			response.setMessage(HttpStatus.OK.getReasonPhrase());
		} else {
			response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
			response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(BaseConfiguration.BASE_API_URL + "/users-role/{username}")
	public @ResponseBody Response updateUserRole(@PathVariable String username,  @RequestBody Map<String, Object> context) {
		Response response = new Response(200);
		List<String> newListRoles = (List<String>) context.get("listRoles");
		if(!newListRoles.isEmpty()) {
			ldapUserService.updateUserRole(username, newListRoles);
		}
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/users-role/{username}")
	public @ResponseBody Response getRole(@PathVariable String username) {
		Response response = new Response(200);
		Map<String, Object> result = new HashMap<String, Object>();
		UserDTO user = ldapUserService.getUserDetail(username);
		result.put("role", user.getUserRole());
		return response;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/search-users/{name}")
	public @ResponseBody Response searchUsers(@PathVariable String name) {
		Response response = new Response(200);
		if (name != null && !name.equals("")) {
			if (!UtilsValidate.containRegex(name)) {
				List<UserDTO> listUsers = ldapUserService.findUsersByName(name);
				if (!listUsers.isEmpty()) {
					response.setData(listUsers);
					response.setMessage(HttpStatus.OK.getReasonPhrase());
				} else {
					response.setMessage(ErrorCode.NOT_FIND_USER.getContent());
					response.setStatus(ErrorCode.NOT_FIND_USER.getValue());
				}
			} else {
				response.setMessage(ErrorCode.PARAMETERS_IS_NOT_VALID.getContent());
				response.setStatus(ErrorCode.PARAMETERS_IS_NOT_VALID.getValue());
			}
		} else {
			response.setMessage(ErrorCode.PARAMETERS_IS_MISSING.getContent());
			response.setStatus(ErrorCode.PARAMETERS_IS_MISSING.getValue());
		}
		return response;
	}

}
