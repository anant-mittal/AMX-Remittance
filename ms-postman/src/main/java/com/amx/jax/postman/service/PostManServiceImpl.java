package com.amx.jax.postman.service;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.bootloaderjs.JsonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManServiceImpl implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManServiceImpl.class);

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private EmailService emailService;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private TemplateEngine textTemplateEngine;

	@Autowired
	private SMService smsService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private PdfService pdfService;

	public Email sendEmail(Email email) {

		if (email.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(email.getModel());
			if (email.isHtml()) {
				email.setMessage(templateEngine.process(email.getTemplate(), context));
			} else {
				email.setMessage(textTemplateEngine.process(email.getTemplate(), context));
			}
		}
		return emailService.send(email);
	}

	@Override
	public File processTemplate(String template, Object data, Type fileType) throws UnirestException {
		Map<String, Object> map = JsonUtil.toMap(data);
		return this.processTemplate(template, map, fileType);
	}

	public File processTemplate(String template, Map<String, Object> map, Type fileType) {
		File file = new File();
		Context context = new Context();
		context.setVariables(map);
		try {
			file.setContent(templateEngine.process(template, context));
		} catch (Exception e) {
			LOGGER.error("Template {}", template, e);
		}
		if (fileType == Type.PDF) {
			file.setName(template + ".pdf");
			pdfService.convert(file);
		} else {
			file.setName(template + ".html");
		}
		return file;
	}

	public SMS sendSMS(SMS sms) throws UnirestException {
		if (sms.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(sms.getModel());
			sms.setMessage(templateEngine.process(sms.getTemplate(), context));
		}
		return this.smsService.sendSMS(sms);
	}

	@Override
	public Message notifySlack(Message msg) throws UnirestException {
		return slackService.sendNotification(msg);
	}

}
