package com.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.entity.User;
import com.service.MailSender;
import com.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired
	UserService userService;
	
	@Autowired
    BCryptPasswordEncoder bryptEncoder;

	
	@GetMapping("/")
	public String userForm(Model model) {
		model.addAttribute("user", new User());
		return "User_form";
	}
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("title","LoginPage");
		return "login";
	}
	
	@PostMapping("/register")
	public String userRegister(@Valid @ModelAttribute("user") User user,BindingResult bindingResult, @RequestParam("image") MultipartFile file,Model model) throws IOException{
		
		if(!file.isEmpty()) {
			
			user.setImage(file.getOriginalFilename());
			
			File saveFile = new ClassPathResource("static/img").getFile();
			
			Path path = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());
			
			Files.copy(file.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);

			System.out.println("image uploaded");
			 
		}
		else {
			  user.setImage("contact.png");
		}
		
		      user.setEnabled(true);
		
		      user.setPassword(bryptEncoder.encode(user.getPassword()));
		      
		     userService.saveUser(user);
		    
		     System.out.println("registration successfull");
		     
		     model.addAttribute("user",new User());
     
		return "User_form";
	}
	

	
	
}
