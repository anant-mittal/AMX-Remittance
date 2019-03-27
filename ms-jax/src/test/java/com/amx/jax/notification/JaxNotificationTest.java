package com.amx.jax.notification;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.amx.jax.postman.model.Email;
import com.amx.jax.services.JaxNotificationService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JaxNotificationTest {

	@Autowired
	JaxNotificationService service;

	@Test
	public void testSendEmailAsyc() {

		Email email = new Email();
		email.addTo("prashantnitdgp@gmail.com");
		email.setMessage("testing");
		service.sendEmail(email);
	}
}
