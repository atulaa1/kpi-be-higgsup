package com.higgsup.kpi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.higgsup.kpi.dto.UserDTO;
import com.higgsup.kpi.entity.KpiUser;
import com.higgsup.kpi.repository.UserRepository;
import com.higgsup.kpi.service.LdapUserService;
import com.higgsup.kpi.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired 
	private UserRepository userRepository;
	
	@Autowired
	private LdapUserService ldapUserService;
	
	@Override
	public KpiUser getUserDetails(String username) {
		KpiUser user = userRepository.findByUserName(username);
		if(user != null) {
			return user;
		}
		return null;
	}

	@Override
	public KpiUser registUser(String username) {
		KpiUser user = userRepository.findByUserName(username);
		if(user == null) {
			UserDTO ldapUser = ldapUserService.getUserDetail(username);
			if(ldapUser != null) {
				KpiUser kpiUser = new KpiUser();
				kpiUser.setUserName(ldapUser.getUsername());
				kpiUser.setEmail(ldapUser.getEmail());
				kpiUser.setFirstName(ldapUser.getFirstName());
				kpiUser.setLastName(ldapUser.getLastName());
				kpiUser.setActive(1);
				userRepository.save(kpiUser);
			}
		}
		return null;
	}

}
