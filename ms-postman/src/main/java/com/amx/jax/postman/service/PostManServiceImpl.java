package com.amx.jax.postman.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.bootloaderjs.JsonUtil;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManServiceImpl implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManServiceImpl.class);

	@Autowired
	private EmailService emailService;

	@Autowired
	private SMService smsService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private PdfService pdfService;

	@Autowired
	private FileService fileService;

	@Autowired
	private TemplateService templateService;

	@Async
	public Email sendEmail(Email email) {

		if (email.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(email.getModel());
			if (email.isHtml()) {
				email.setMessage(templateService.processHtml(email.getTemplate(), context));
			} else {
				email.setMessage(templateService.processText(email.getTemplate(), context));
			}
		}

		if (email.getFiles() != null && email.getFiles().size() > 0) {
			for (File file : email.getFiles()) {
				fileService.create(file);
			}
		}

		return emailService.send(email);
	}

	@Override
	public File processTemplate(Templates template, Object data, Type fileType) throws UnirestException {
		Map<String, Object> map = JsonUtil.toMap(data);
		return this.processTemplate(template, map, fileType);
	}

	public File processTemplate(Templates template, Map<String, Object> map, Type fileType) {
		File file = new File();
		Context context = new Context();
		context.setVariables(map);
		try {
			file.setContent(templateService.processHtml(template, context));
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

	@Async
	public SMS sendSMS(SMS sms) throws UnirestException {
		if (sms.getTemplate() != null) {
			Context context = new Context();
			context.setVariables(sms.getModel());
			sms.setMessage(templateService.processHtml(sms.getTemplate(), context));
		}
		return this.smsService.sendSMS(sms);
	}

	@Override
	@Async
	public Message notifySlack(Message msg) throws UnirestException {
		return slackService.sendNotification(msg);
	}

}
