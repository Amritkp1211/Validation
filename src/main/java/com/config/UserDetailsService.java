package com.config;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.entity.User;
import com.repository.UserRepository;

public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username /*email*/) throws UsernameNotFoundException {
      
		User user = userRepository.getUserByUserName(username);
	
		if(user==null) {
			throw new UsernameNotFoundException("could not found user");
		
		}
		 System.out.println(user.toString());
		 com.config.UserDetails userDetails=new com.config.UserDetails(user);
		
		return userDetails;
	}

}
