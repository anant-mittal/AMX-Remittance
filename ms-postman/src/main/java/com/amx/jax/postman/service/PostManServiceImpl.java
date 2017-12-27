package com.amx.jax.postman.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.bootloaderjs.JsonUtil;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManServiceImpl implements PostManService {

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
			sms.setMessage(templateEngine.process(sms.getTemplate(), context));
		}
		this.smsService.sendSMS(sms);
	}

	@Override
	public void notifySlack(Message msg) throws UnirestException {
		slackService.sendNotification(msg);
	}

	@Override
	public void downloadPDF(String template, Object data, String fileName) throws IOException, DocumentException {
		File file = this.processTemplate(template, data, fileName);
		file.donwload(response, true);
	}

	@Override
	public void createPDF(String template, Object data) throws IOException, DocumentException {
		File file = this.processTemplate(template, data, null);
		file.donwload(response, false);
	}

}
