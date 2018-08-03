package com.higgsup.kpi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.higgsup.kpi.configure.BaseConfiguration;
import com.higgsup.kpi.entity.UserDTO;
import com.higgsup.kpi.services.UserService;

@RestController
@CrossOrigin("*")
public class UserController {

	@Autowired
	private UserService userSerivce;
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/getUser")
	public String getTestUser() {
		return "Test";
	}

}
