package com.service;


public interface MailSender {

	boolean SendEmail(String to ,String message, String subject);
}
