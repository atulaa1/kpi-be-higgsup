package com.higgsup.kpi.configure;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.configurers.ldap.LdapAuthenticationProviderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.higgsup.kpi.configure.jwt.JWTAuthenticateFilter;
import com.higgsup.kpi.configure.jwt.JWTLoginFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigure extends WebSecurityConfigurerAdapter  {
	
	
	//Get Config Value
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
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors()
		.and()	
			.authorizeRequests()
			.antMatchers(HttpMethod.POST, BaseConfiguration.BASE_API_URL+"/login").permitAll()
			.antMatchers(BaseConfiguration.BASE_API_URL+"/**").fullyAuthenticated()
			.anyRequest().fullyAuthenticated()
		.and()
			.addFilterBefore(new JWTLoginFilter(BaseConfiguration.BASE_API_URL+"/login", authenticationManager()),
	                UsernamePasswordAuthenticationFilter.class)
	        .addFilterBefore(new JWTAuthenticateFilter(),
	                UsernamePasswordAuthenticationFilter.class)
    		.logout()
    		.invalidateHttpSession(true)
    		.deleteCookies("JSESSIONID")
    		.permitAll();
	}

	@Configuration
    protected class AuthenticationConfiguration extends GlobalAuthenticationConfigurerAdapter {
        @Override
        public void init(AuthenticationManagerBuilder auth) throws Exception {              
        	LdapContextSource contextSource = new LdapContextSource();
        	contextSource.setUrl(baseUrl);
            contextSource.setBase(baseDN);
            contextSource.setUserDn(baseUserDN);
            contextSource.setPassword(password);
            contextSource.setReferral("follow"); 
            contextSource.afterPropertiesSet();

            LdapAuthenticationProviderConfigurer<AuthenticationManagerBuilder> ldapAuthenticationProviderConfigurer = auth.ldapAuthentication();

            ldapAuthenticationProviderConfigurer
            	.userSearchBase(searchBase)
                .userSearchFilter(userSearchBase)
                .contextSource(contextSource);
//            .and().userDetailsService(userDetailsService);
        }
    }
}
