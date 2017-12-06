package com.amx.jax.postman;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class AppMailSender implements ApplicationRunner {

	@Autowired
	EmailService emailService;

	@Autowired
	private TemplateEngine templateEngine;

	@Override
	public void run(ApplicationArguments args) throws Exception {

		sendHtmltMail();
		sendTextMail();

	}

	private void sendTextMail() {

		String from = "lalit.tanwar.almulla@gmail.com"; 
		String to = "lalit.tanwar07@gmail.com";
		
		String subject = "Java Mail with Spring Boot - Plain Text";

		EmailTemplate template = new EmailTemplate("hello-world-plain.txt");

		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", "Pavan");
		replacements.put("today", String.valueOf(new Date()));

		String message = template.getTemplate(replacements);

		Email email = new Email(from, to, subject, message);

		emailService.send(email);
	}

	private void sendHtmltMail() {

		String from = "lalit.tanwar.almulla@gmail.com"; 
		String to = "lalit.tanwar07@gmail.com";
		
		String subject = "Java Mail with Spring Boot";

		Context context = new Context();
		context.setVariable("user", "Pavan");
		context.setVariable("today", String.valueOf(new Date()));

		String message = templateEngine.process("hello-world", context);

		Email email = new Email(from, to, subject, message);

		email.setHtml(true);
		emailService.send(email);
	}

}