package com.lxisoft.vegetablestore.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetail;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/login").permitAll().antMatchers("/select-vegetable/*", "/delete-vegetable/*")
                .access("hasRole('ROLE_ADMIN')").anyRequest().authenticated().and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/").and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
    }

    protected void configure (AuthenticationManagerBuilder auth) throws Exception{

        auth.userDetailsService(userDetail).passwordEncoder(new BCryptPasswordEncoder());
    }


}
