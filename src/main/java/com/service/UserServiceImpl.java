package com.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.entity.User;
import com.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void saveUser(User user) {
	
		userRepository.save(user);
	}

	
	public boolean emailSend(String message, String to, String subject) {
	
		boolean flag=false;
		
		String from="";
	    String host="smtp.gmail.com";
	    
	    Properties properties = System.getProperties();
		   properties.put("mail.smtp.host", host);
		   properties.put("mail.smtp.port", "465");
		   properties.put("mail.smtp.ssl.enable",true);
		   properties.put("mail.smtp.auth", "true");
		  
		   Session session=Session.getInstance(properties, new Authenticator() {          
			   
			   @Override
			   protected PasswordAuthentication getPasswordAuthentication() {
				   return new PasswordAuthentication("amritkptectona@gmail.com", "rsccguavtgtgaamy");
			   }	   
		   });
		  
		   session.setDebug(true);
		   
		   //compose mail
		   MimeMessage mime=new MimeMessage(session);
		    
		   try {
			mime.setContent(message,"text/html");
			
			mime.addRecipient(RecipientType.TO,new InternetAddress(to));
			
			mime.setFrom(from);
			
			mime.setSubject(subject);
			
			Transport.send(mime);
			
			System.out.println("otp send successfully");
			
			flag=true;
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
		  
		return flag;
		
		
	
	}

}
