package com.payroll.example.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

@Configuration
@EnableResourceServer
public class SecurityConfig extends ResourceServerConfigurerAdapter {

	@Value("${jwk-url}")
	private String jwk_url;
@Bean
public TokenStore tokenStore() {
	return new JwkTokenStore(jwk_url);
}

@Override
public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
	resources.tokenStore(tokenStore()).resourceId("payroll");
}




}
