package com.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.entity.Contact;
import com.entity.User;
import com.repository.ContactRepository;
import com.repository.UserRepository;

@RestController
public class SearchController {

	@Autowired
	ContactRepository contactRepository;
	
	@Autowired
	UserRepository userRepository;
	
	//search controller
	@GetMapping("/search/{query}")
	public ResponseEntity<?> searchMethod(@PathVariable("query") String query,Principal principal){
		
		System.out.println("Query:" +query);
		
		   User user = userRepository.getUserByUserName(principal.getName());
		   
		   List<Contact> contacts = contactRepository.findByNameContainingAndUser(query, user);

		return ResponseEntity.ok(contacts);
		
	}
	
	
	
	
}
