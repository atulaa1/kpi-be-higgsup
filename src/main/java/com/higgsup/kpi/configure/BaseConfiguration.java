package com.higgsup.kpi.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
public class BaseConfiguration {

	// Config API
	public static final String BASE_API_URL = "/api";

	// Config JWT
	public static final long BASE_TIMEOUT_TOKEN = 60000;

	public static final String BASE_SECRET_VALUE_TOKEN = "2r4w3esfD";

	// Get base LDAP config
	@Value("${ldap.baseUrl}")
	private String baseUrl;

	@Value("${ldap.baseDN}")
	private String baseDN;

	@Value("${ldap.baseUserDN}")
	private String baseUserDN;

	@Value("${ldap.password}")
	private String password;

	@Value("${ldap.searchBase}")
	private String searchBase;

	@Value("${ldap.userSearchBase}")
	private String userSearchBase;

	// Ldap Template context source
	
	@Bean
	public LdapContextSource contextSource() {
		LdapContextSource contextSource = new LdapContextSource();
		contextSource.setUrl(baseUrl);
        contextSource.setBase(baseDN);
        contextSource.setUserDn(baseUserDN);
        contextSource.setPassword(password);
        contextSource.setReferral("follow"); 
        contextSource.afterPropertiesSet();
		return contextSource;
	}

	@Bean
	public LdapTemplate ldapTemplate() {
		return new LdapTemplate(contextSource());
	}

}
