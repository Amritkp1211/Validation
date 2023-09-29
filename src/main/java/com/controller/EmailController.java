package com.controller;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.MailSender;

@RestController
public class EmailController {

	
	@Autowired
	MailSender mailSender;
	
	@PostMapping("/email")
	public String sendEmail() {
		
		Random random=new Random();
		int otp = random.nextInt(10000);
		
		String to="amritkp12112000@gmail.com";
		String message="welcome back Prabhas"+otp+" ";
		String subject="welcome";
		
		 mailSender.SendEmail(to, message, subject);
	   return "sent successfully";
		
	}
}
