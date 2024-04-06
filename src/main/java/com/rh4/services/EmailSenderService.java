package com.rh4.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("mail sent");
	}
	
}
