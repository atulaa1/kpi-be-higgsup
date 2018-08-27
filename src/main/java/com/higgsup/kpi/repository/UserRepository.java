package com.higgsup.kpi.repository;

import org.springframework.data.repository.CrudRepository;

import com.higgsup.kpi.entity.KpiUser;

public interface UserRepository extends CrudRepository<KpiUser, String>{
	KpiUser findByUserName(String username);
}
