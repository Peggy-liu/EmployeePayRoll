package com.payroll.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.payroll.example.service.MyUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MyUserDetailService service;
//	
//	@Autowired
//	JwtAuthenticationFilter authenticationFilter;
	


	@Autowired
	JwtAuthorizationFilter authorizationFilter;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.authenticationProvider(getProvider());
		auth.inMemoryAuthentication().withUser("user").password("user")
				.authorities(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder encoder() {
		return NoOpPasswordEncoder.getInstance();
	}

	@Bean
	public DaoAuthenticationProvider getProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(service);
		return provider;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.addFilterAt(new JwtAuthenticationFilter(authenticationManagerBean()),UsernamePasswordAuthenticationFilter.class)
		        //.addFilterAt(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
				.addFilterBefore(authorizationFilter, UsernamePasswordAuthenticationFilter.class).authorizeRequests()
				.antMatchers("/login").permitAll().antMatchers("/orders/**").hasRole("USER")
				.antMatchers("/employees/**").hasRole("ADMIN");
	
		// .anyRequest().authenticated();

	}

}
