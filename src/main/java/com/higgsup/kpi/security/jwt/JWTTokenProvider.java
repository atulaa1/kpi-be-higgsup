package com.higgsup.kpi.security.jwt;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.higgsup.kpi.configure.BaseConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JWTTokenProvider {
	//1440 is 1440 minute is 1 day
	private static final long EXPIRATIONTIME = BaseConfiguration.BASE_TIMEOUT_TOKEN * 1440; // base time out is 6000ms 1 minute

	static void addAuthentication(HttpServletResponse res, String username, Authentication auth) throws IOException {
		Claims claims = Jwts.claims().setSubject(username);

		if (username != null && username.length() > 0) {
			claims.put("authorities", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toSet()));
		}
		String JWT = Jwts.builder().setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, BaseConfiguration.BASE_SECRET_VALUE_TOKEN).compact();
				
		res.addHeader(BaseConfiguration.HEADER_STRING_AUTHORIZATION, BaseConfiguration.TOKEN_PREFIX + " " + JWT);

		ObjectMapper mapper = new ObjectMapper();

		Map<String, String> tokenMap = new HashMap<>();
		tokenMap.put("username", username);
		res.setContentType(MediaType.APPLICATION_JSON_VALUE);

		mapper.writeValue(res.getWriter(), tokenMap);

	}

	@SuppressWarnings("unchecked")
	static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String token = request.getHeader(BaseConfiguration.HEADER_STRING_AUTHORIZATION);
		if (token != null) {
			String user = null;
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			try {
				Claims claims = Jwts.parser().setSigningKey(BaseConfiguration.BASE_SECRET_VALUE_TOKEN)
						.parseClaimsJws(token.replace(BaseConfiguration.TOKEN_PREFIX, "")).getBody();
				user = claims.getSubject();
				List<String> tmpAuthorities = (List<String>) claims.get("authorities");
				for(String item : tmpAuthorities) {
					grantedAuthorities.add(new SimpleGrantedAuthority(item));
				}
			} catch (IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			} catch (ExpiredJwtException e1) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e1.getMessage());
			} catch (SignatureException e2) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e2.getMessage());
			}
			return user != null ? new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities) : null;
		}
		return null;
	}
}
