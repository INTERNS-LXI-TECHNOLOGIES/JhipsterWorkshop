package com.lxisoft.vegetablestore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import tech.jhipster.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.lxisoft.vegetablestore.security.DomainUserDetailsService;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	@Autowired
	private UserDetailsService userDetail;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
        return new AjaxAuthenticationSuccessHandler();
    }

    @Bean
    public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
        return new AjaxAuthenticationFailureHandler();
    }

    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }

	
	
	
	
	protected void configure(HttpSecurity http) throws Exception 
	{
		
		http.csrf().disable().authorizeRequests().antMatchers("/login").permitAll()
				.antMatchers("/select-vegetable/*", "/delete-vegetable/*").access("hasRole('ROLE_ADMIN')").anyRequest()
				.authenticated().and().formLogin()
	           .loginPage("/login").defaultSuccessUrl("/").and()
	            .logout()
	            .logoutUrl("/logout")
	            .logoutSuccessHandler(ajaxLogoutSuccessHandler());
	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		

		auth.userDetailsService(userDetail).passwordEncoder(new BCryptPasswordEncoder());
	}

}