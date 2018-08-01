package com.higgsup.kpi.configure.jwt;

import static java.util.Collections.emptyList;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.higgsup.kpi.configure.BaseConfiguration;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWTTokenService {

	static final long EXPIRATIONTIME = BaseConfiguration.BASE_TIMEOUT_TOKEN; // 6000ms 1 minute
	static final String TOKEN_PREFIX = "Bearer";
	static final String HEADER_STRING = "Authorization";

	static void addAuthentication(HttpServletResponse res, String uid) throws IOException {
		String JWT = Jwts.builder().setSubject(uid).setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
				.signWith(SignatureAlgorithm.HS512, BaseConfiguration.BASE_SECRET_VALUE_TOKEN).compact();
		res.addHeader(HEADER_STRING, TOKEN_PREFIX + " " + JWT);
	}

	static Authentication getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String token = request.getHeader(HEADER_STRING);
		if (token != null) {
			String user = "";
			try {
				user = Jwts.parser().setSigningKey(BaseConfiguration.BASE_SECRET_VALUE_TOKEN)
						.parseClaimsJws(token.replace(TOKEN_PREFIX, "")).getBody().getSubject();
			} catch (ExpiredJwtException ex) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
			}
			return user != null ? new UsernamePasswordAuthenticationToken(user, null, emptyList()) : null;
		}
		return null;
	}
}
