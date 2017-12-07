package com.amx.jax.postman.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.EmailService;
import com.amx.jax.postman.JaxFile;
import com.bootloaderjs.JsonUtil;

@Component
public class PostManService {

	@Autowired
	private EmailService emailService;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private TemplateEngine textTemplateEngine;

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

	public JaxFile processTemplate(String template, Object data, String fileName) {
		JaxFile file = new JaxFile();
		Map<String, Object> map = JsonUtil.toMap(data);
		Context context = new Context();
		context.setVariables(map);
		file.setContent(templateEngine.process(template, context));
		file.setName(fileName);
		return file;
	}

}
