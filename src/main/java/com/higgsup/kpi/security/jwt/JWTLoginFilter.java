package com.higgsup.kpi.security.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.higgsup.kpi.entity.UserDTO;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	@Autowired
	private UserDetailsService userDetailsService;

	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException, IOException, ServletException {
		
		UserDTO user = new ObjectMapper().readValue(req.getInputStream(), UserDTO.class);
		
		UserDetails userDetails = null;
		if(user != null) {
			userDetails = userDetailsService.loadUserByUsername(user.getUsername());
		}
		
		Authentication auth = getAuthenticationManager()
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), userDetails.getAuthorities()));

		SecurityContextHolder.getContext().setAuthentication(auth);
		
		return auth;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Authentication auth1 = auth;
		JWTTokenProvider.addAuthentication(res, auth.getName(), auth);
	}
}
