package com.higgsup.kpi.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@PropertySource("classpath:config.properties")
public class BaseConfiguration {

    // Config API
    public static final String BASE_API_URL = "/api";

    // Config JWT
    public static final long BASE_TIMEOUT_TOKEN = 60000;
    public static final String BASE_SECRET_VALUE_TOKEN = "2r4w3esfD";
    public static final String HEADER_STRING_AUTHORIZATION = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";

    @Autowired
    private Environment environment;

    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl(environment.getProperty("ldap.baseUrl"));
        contextSource.setBase(environment.getProperty("ldap.baseDN"));
        contextSource.setUserDn(environment.getProperty("ldap.baseUserDN"));
        contextSource.setPassword(environment.getProperty("ldap.password"));
        contextSource.setReferral("follow");
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
        return new LdapTemplate(contextSource());
    }

}
