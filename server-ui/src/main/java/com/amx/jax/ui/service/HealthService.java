package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Templates;

@Service
public class HealthService {

	private Logger log = Logger.getLogger(HealthService.class);

	@Value("${application.name}")
	private String appName;

	@Autowired
	private PostManService postManService;

	@Autowired
	AppEnvironment environment;

	@Async
	public void sendApplicationLiveMessage() {

		Message msg = new Message();

		if (environment.isDebug()) {
			log.info("Server is in debug mode");
			return;
		}

		try {
			msg.setMessage(appName + "\n is Up and Runnnig.");
			postManService.notifySlack(msg);
		} catch (Exception e) {
			log.error("Error while sending Notification to Slack", e);
		}

		Email email = new Email();
		email.setSubject("UI Server is UP");
		email.setFrom("amxjax@gmail.com");
		email.setTo("riddhi.madhu@almullagroup.com");

		email.setTemplate(Templates.SERVER_UP);
		email.setHtml(true);

		try {
			postManService.sendEmail(email);
			log.info("Sending Server up emailt to admi");
		} catch (Exception e) {
			log.error("Error while sending UP Email to riddhi.madhu@almullagroup.com", e);
		}
	}

}
