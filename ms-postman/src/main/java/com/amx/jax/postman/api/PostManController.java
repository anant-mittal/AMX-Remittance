package com.amx.jax.postman.api;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.Templates;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

@RestController
public class PostManController {

	@Autowired
	private PostManService postManService;

	private Logger logger = Logger.getLogger(PostManController.class);

	@RequestMapping(value = "/pub/postman/template/{template}", method = RequestMethod.GET)
	public String showtemplate(@PathVariable String template, HttpServletResponse resp)
			throws IOException, DocumentException, UnirestException {
		Email em = new Email();
		em.setFrom("amxjax@gmail.com");
		em.setTo("lalit.tanwar07@gmail.com");
		em.getModel().put("otp", "3434");
		em.setTemplate(Templates.RESET_OTP);
		em.setHtml(true);
		em.setSubject("tes Email");
		postManService.sendEmail(em);

		SMS sms = new SMS();

		sms.setTo("8587874877");
		sms.setTemplate(Templates.RESET_OTP_SMS);
		sms.getModel().put("otp", "3434");

		postManService.sendSMS(sms);

		return postManService.processTemplate(template, em.getModel(), null).getContent();
	}

}
