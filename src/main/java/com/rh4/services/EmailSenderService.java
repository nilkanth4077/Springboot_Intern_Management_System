package com.rh4.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

	Logger logger = LoggerFactory.getLogger(EmailSenderService.class);

	@Autowired
	private JavaMailSender mailSender;
	
	public EmailSenderService(JavaMailSender mailSender) {
		super();
		this.mailSender = mailSender;
	}

	@Value("${spring.mail.username}")
	private String fromEmail;

	public void sendSimpleEmail(String toEmail,String body,String subject)
	{
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		message.setFrom(fromEmail);
		
		try {
			mailSender.send(message);
		} catch (MailException e) {
			logger.info("Error while sending the email: " + e.getMessage());
		}
	}
	
}
