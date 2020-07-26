package com.payroll.example.security;

import org.springframework.security.authentication.AuthenticationManager;

public class JwtProperties {

	public static final String SECRET = "secret";
	public static final String HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final int EXPIRATION_TIME = 864000000;
}
