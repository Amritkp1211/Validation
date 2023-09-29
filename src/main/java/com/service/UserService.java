package com.service;



import com.entity.User;

public interface UserService {

	void saveUser(User user);
	
	boolean emailSend(String message,String to,String subject);
	
}
