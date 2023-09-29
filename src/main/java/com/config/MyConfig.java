package com.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class MyConfig {
	
	

	@Bean
	public UserDetailsService getUserDetailService() {
		return new com.config.UserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordencoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider daoAuthProvider() {
		    DaoAuthenticationProvider daoProvider=new DaoAuthenticationProvider();
		    
		     daoProvider.setUserDetailsService(getUserDetailService());
		     daoProvider.setPasswordEncoder(passwordencoder());
		
		     return daoProvider;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	
		 
	
		
		 http.csrf().disable() .authorizeHttpRequests().
               requestMatchers("/user/**").hasRole("USER")
              .requestMatchers("/admin/**").hasRole("ADMIN")
              .requestMatchers("/**").permitAll() .anyRequest().authenticated() .and()
              .formLogin()
              .loginPage("/login")
              .loginProcessingUrl("/dologin")
              .defaultSuccessUrl("/user/index");
		 
	       http.authenticationProvider(daoAuthProvider());
		return http.build();
	
    }
}