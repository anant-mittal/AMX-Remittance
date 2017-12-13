package com.amx.jax.postman.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.EmailService;
import com.amx.jax.postman.File;
import com.amx.jax.postman.SMS;
import com.bootloaderjs.JsonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private TemplateEngine textTemplateEngine;

	@Autowired
	private SMService smsService;

	public void sendEmail(Email email) {

		if (email.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(email.getModel());
			if (email.isHtml()) {
				email.setMessage(templateEngine.process(email.getTemplate(), context));
			} else {
				email.setMessage(textTemplateEngine.process(email.getTemplate(), context));
			}
		}
		emailService.send(email);
	}

	public File processTemplate(String template, Object data, String fileName) {
		File file = new File();
		Map<String, Object> map = JsonUtil.toMap(data);
		Context context = new Context();
		context.setVariables(map);
		file.setContent(templateEngine.process(template, context));
		file.setName(fileName);
		return file;
	}

	public void sendSMS(SMS sms) throws UnirestException {
		if (sms.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(sms.getModel());
			sms.setText(templateEngine.process(sms.getTemplate(), context));
		}
		this.smsService.sendSMS(sms);
	}

}
