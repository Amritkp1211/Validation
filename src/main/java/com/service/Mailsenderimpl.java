package com.service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class Mailsenderimpl implements MailSender{

	@Override
	public boolean SendEmail(String to ,String message, String subject) {

		boolean flag=false;
		
	    String from="";
		
         Properties props=System.getProperties();
           props.put("mail.smtp.port","465" );
           props.put("mail.smtp.host", "smtp.gmail.com");
           props.put("mail.smtp.ssl.enable", "true");
           props.put("mail.smtp.auth",true);
		
           
           //1 to get session object
           Session session=Session.getInstance(props, new Authenticator() {		
        	   protected PasswordAuthentication getPasswordAuthentication() {
        		   return new PasswordAuthentication("amritkptectona@gmail.com", "rsccguavtgtgaamy");
        	   }
        	 });
           
           session.setDebug(true);
           
           //2 set content 
            
           MimeMessage m=new MimeMessage(session);
           
            try {
				m.setFrom(from);
				
				m.setContent(message,"text/html");
				
				m.setSubject(subject);
				
				m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
				
				Transport.send(m);
				
				System.out.println("send successfully");
				
				flag=true;
				
			} catch (MessagingException e) {
				
				e.printStackTrace();
			}
            
           
           
           return flag;
	}

}
