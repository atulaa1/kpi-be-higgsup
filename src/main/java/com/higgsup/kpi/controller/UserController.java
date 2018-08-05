package com.higgsup.kpi.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.higgsup.kpi.configure.BaseConfiguration;

@RestController
@CrossOrigin("*")
public class UserController {

	
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	@GetMapping(BaseConfiguration.BASE_API_URL + "/getUser")
	public String getTestUser() {
		return "Test";
	}

}
