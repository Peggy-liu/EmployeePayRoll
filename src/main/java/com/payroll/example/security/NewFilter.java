package com.payroll.example.security;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.example.model.AuthRequest;



public class NewFilter extends OncePerRequestFilter {
	
	@Autowired
	private AuthenticationManager manager;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		AuthRequest authRequest = new ObjectMapper().readValue(request.getReader(), AuthRequest.class);
		if(authRequest!=null) {
			Authentication auth = new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword());
			Authentication result = manager.authenticate(auth);
			if(result.isAuthenticated()==true) {
				String token = JWT.create()
								.withSubject(result.getName())
								.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
								.sign(Algorithm.HMAC512(JwtProperties.SECRET));
				response.getWriter().append(token);
				response.setStatus(200);
				
				
			}
			else {
				response.setStatus(401);
			}
			
		}
		filterChain.doFilter(request, response);

	}

}
