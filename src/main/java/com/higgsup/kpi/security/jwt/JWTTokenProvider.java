package com.higgsup.kpi.security.jwt;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.higgsup.kpi.configure.BaseConfiguration;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

public class JWTTokenProvider {

	static final long EXPIRATIONTIME = BaseConfiguration.BASE_TIMEOUT_TOKEN*30; // base time out is 6000ms 1 minute
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse res, String username, Authentication auth) throws IOException {
		Claims claim = Jwts.claims().setSubject(username);
		
		if ( username != null && username.length() > 0 ) {
		    claim.put("roles", auth.getAuthorities());
		}
		
		String JWT = Jwts.builder().setClaims(claim).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, BaseConfiguration.BASE_SECRET_VALUE_TOKEN).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String user = null;
			try {
				user = Jwts.parser().setSigningKey(BaseConfiguration.BASE_SECRET_VALUE_TOKEN)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
			} catch (IllegalArgumentException e) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
			} catch (ExpiredJwtException e1) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e1.getMessage());
			} catch (SignatureException e2) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e2.getMessage());
			}
			return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
		}
		return null;
	}
}
