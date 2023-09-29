package com.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.entity.User;
import com.repository.UserRepository;
import com.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ForgotController {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserService userService;
	
	@Autowired
	BCryptPasswordEncoder brEncoder;
	
	@GetMapping("/forgot")
	public String forgotPassword(Model model) {
		model.addAttribute("title","Forgot Password");
		
		return "forgot_password";
	}
	
	///send-otp
	@PostMapping("/send-otp")
	public String sendOtp(HttpSession session,@RequestParam("email") String email){
		System.out.println("email: email");
		
		Random random=new Random();
		
		 int otp = random.nextInt(10000);
	
		 System.out.println("otp:" +otp);
		 
		 String message=""+otp;
		 
		 String to=email;
		 
		 String subject="OTP FROM Validation";
		 
		 boolean flag = userService.emailSend(message, to, subject);
		 
		 if(flag) {
			 session.setAttribute("otp",otp);
			 session.setAttribute("email", email);
		 }
		 
		return "submit_otp";
	}
	
	@PostMapping("/verify-otp")
	public String verifyOtp(HttpSession session, @RequestParam("myotp") int myotp){
		
	     int otp = (int)session.getAttribute("otp");
	     String email= (String) session.getAttribute("email");
	     
	     if(otp==myotp) {
	    	 User user = userRepository.getUserByUserName(email);
	    	 
	    	 if(user==null) {
	    		  session.setAttribute("message","User with this email doesn't exits !!");
		                			
	               return "forgot_password_form";	
	    	 }
	    	 else {
	    		 System.out.println("change passsword form");
	    
	    		 return "change_password";
	    	 }
	    	 
	     }
		

		return "change_password";
	}
	
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("newpassword") String newpassword, HttpSession session) {
		
		  String email=(String) session.getAttribute("email");
		  
		  User user = userRepository.getUserByUserName(email);
		  
		   user.setPassword(brEncoder.encode(newpassword));
		   
		   System.out.println("password changes successfulyy" +newpassword);
		   userService.saveUser(user);
		
		
		System.out.println("password changes successfully");
		return "login";
	}

	
	
}
