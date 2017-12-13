package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.Templates;
import com.amx.jax.postman.client.PostManClient;

@Service
public class HealthService {

	private Logger log = Logger.getLogger(HealthService.class);

	@Autowired
	private PostManClient postManClient;

	@Async
	public void sendApplicationLiveMessage() {
		SMS sms = new SMS();

		try {
			sms.setTo("7710072192");
			sms.setText("Your OTP for Reset is");
			sms.setTemplate(Templates.RESET_OTP_SMS);
			// postManClient.sendSMS(sms);
		} catch (Exception e) {
			log.error("Error while sending OTP SMS to 7710072192", e);
		}

		Email email = new Email();
		email.setSubject("UI Server is UP");
		email.setFrom("amxjax@gmail.com");
		email.setTo("riddhi.madhu@almullagroup.com");

		email.setTemplate(Templates.SERVER_UP);
		email.setHtml(true);

		try {
			postManClient.sendEmail(email);
		} catch (Exception e) {
			log.error("Error while sending UP Email to riddhi.madhu@almullagroup.com", e);
		}
	}

}
