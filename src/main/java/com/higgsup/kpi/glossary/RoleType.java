package com.higgsup.kpi.glossary;

public enum RoleType {
	ADMIN(3, "ROLE_ADMIN"), MAN(2,"ROLE_MAN"), EMPLOYEE(1, "ROLE_EMPLOYEE");

	private Integer priority;
	private String name;

	RoleType(Integer prioriy, String name) {
		this.priority = prioriy;
		this.name = name;
	}
	
	public Integer getPriority() {
		return this.priority;
	}
	
	public String getName() {
		return this.name;
	}
	
	
}
