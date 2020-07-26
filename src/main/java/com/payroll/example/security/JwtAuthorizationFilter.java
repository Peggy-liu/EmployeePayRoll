package com.payroll.example.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.payroll.example.service.MyUserDetailService;


@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private MyUserDetailService service;
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		//extract the token from header
		String header = request.getHeader(JwtProperties.HEADER);
		if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
			filterChain.doFilter(request, response);
			return;
		}
		//validate the token and extract username
		String token = header.replaceAll(JwtProperties.TOKEN_PREFIX, "");
		if(token != null) {
			String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET))
					.build()
					.verify(token)
					.getSubject();
			//get user detail from the database using username
			if(username != null) {
				
				UserDetails user = service.loadUserByUsername(username);
				Authentication auth = new UsernamePasswordAuthenticationToken(username, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
			
		}
			
		filterChain.doFilter(request, response);
		
	}

}
