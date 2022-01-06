package com.alkemy.backend.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;
	
	public String generateToken(UserDetails userDetails) {
		return Jwts.builder().setSubject(userDetails.getUsername()).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 8))
				.signWith(SignatureAlgorithm.HS512, this.secret).compact();
	}
	
	
	public boolean validateToken(String token,UserDetails userDetail) {
		return userDetail.getUsername().equals(this.extractUserName(token)) && !this.isTokenExpired(token);
	}
	
	
	public String extractUserName(String token) {
		return this.getClaims(token).getSubject();
	}
	
	public boolean isTokenExpired(String token) {
		return this.getClaims(token).getExpiration().before(new Date());
	}
	
	private Claims getClaims(String token) {
		return Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
	}
}
