package com.higgsup.kpi.security.jwt;


import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	static final long EXPIRATIONTIME = BaseConfiguration.BASE_TIMEOUT_TOKEN * 30; // base time out is 6000ms 1 minute
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse res, String username, Authentication auth) throws IOException {
		Claims claims = Jwts.claims().setSubject(username);

		if (username != null && username.length() > 0) {
			claims.put("authorities", auth.getAuthorities().stream().map(s -> s.toString()).collect(Collectors.toSet()));
		}
		String JWT = Jwts.builder().setClaims(claims)
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, BaseConfiguration.BASE_SECRET_VALUE_TOKEN).compact();
				
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	@SuppressWarnings("unchecked")
	static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String user = null;
			Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
			try {
				Claims claims = Jwts.parser().setSigningKey(BaseConfiguration.BASE_SECRET_VALUE_TOKEN)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody();
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
