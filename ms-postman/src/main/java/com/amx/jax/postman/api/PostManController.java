package com.amx.jax.postman.api;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.EmailService;

@RestController
public class PostManController {

	@Autowired
	private EmailService emailService;

	private Logger logger = Logger.getLogger(PostManController.class);

	@RequestMapping(value = "/postman/email", method = RequestMethod.POST)
	public Email sendEmail(@RequestParam Email email) {
		logger.debug("loginUser Request: from: " + email.getFrom() + " to: " + email.getTo());
		emailService.send(email);
		return email;
	}

}
