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
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payroll.example.model.AuthRequest;
import org.springframework.util.Assert;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	
	private AuthenticationManager manager;
	

	public JwtAuthenticationFilter(AuthenticationManager manager) {
	
	this.manager = manager;
}

	@Override
	public Authentication attemptAuthentication( HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		//SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, getServletContext());
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
		AuthRequest credential = null;
		
		try {
			credential = new ObjectMapper().readValue(request.getReader(),  AuthRequest.class);
		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("username is "+obtainUsername(request)+" and password is "+ obtainPassword(request));
		Authentication result = manager.authenticate(new UsernamePasswordAuthenticationToken(credential.getUsername(), credential.getPassword()));
		return result;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("here");
		 SecurityContextHolder.getContext().setAuthentication(authResult);
		UserDetails user = (UserDetails) authResult.getPrincipal();
		
		//create Jwt token
		String token = JWT.create()
				.withSubject(user.getUsername())
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(JwtProperties.SECRET));
		
		//send token in the auth response body
		//response.addHeader(JwtProperties.HEADER, JwtProperties.TOKEN_PREFIX+" "+token);
		//response.setStatus(200);
		response.getOutputStream().print(token);;
	    //chain.doFilter(request, response);
		
	}
	
	@Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        logger.debug("failed authentication while attempting to access "
                );

        //Add more descriptive message
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                "Authentication Failed");
    }

//	@Override
//	@Autowired
//	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
//	super.setAuthenticationManager(authenticationManager);
//	}


	
	

}
