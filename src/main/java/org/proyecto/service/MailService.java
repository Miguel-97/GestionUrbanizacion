package org.proyecto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public void sendMail(String remitente, String destinatario, String asunto, String mensaje) {
		
		SimpleMailMessage mail = new SimpleMailMessage();
		
		mail.setFrom(remitente);
		mail.setTo(destinatario);
		mail.setSubject(asunto);
		mail.setText(mensaje);
		
		javaMailSender.send(mail);
	}
	
}
