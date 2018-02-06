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
	public Email sendEmail(Email email) throws UnirestException {
		String to = null;
		try {
			to = email.getTo().get(0);
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
			emailService.send(email);
		} catch (Exception e) {
			this.notifySlack(to, e);
		}
		return email;
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
			LOGGER.error("Template {}", template.getFileName(), e);
			this.notifySlack(template.toString(), e);
		}
		try {
			if (fileType == Type.PDF) {
				file.setName(template.getFileName() + ".pdf");
				pdfService.convert(file);
			} else {
				file.setName(template.getFileName() + ".html");
			}
		} catch (Exception e) {
			this.notifySlack(template.toString(), e);
		}

		return file;
	}

	@Async
	public SMS sendSMS(SMS sms) throws UnirestException {
		String to = null;
		try {
			to = sms.getTo().get(0);
			if (sms.getTemplate() != null) {
				Context context = new Context();
				context.setVariables(sms.getModel());
				sms.setMessage(templateService.processHtml(sms.getTemplate(), context));
			}
			this.smsService.sendSMS(sms);
		} catch (Exception e) {
			this.notifySlack(to, e);
		}
		return sms;
	}

	@Override
	@Async
	public Message notifySlack(Message msg) throws UnirestException {
		return slackService.sendNotification(msg);
	}

	@Async
	public Message notifySlack(String to, Exception e) {
		return slackService.sendNotification(to, e);
	}

	@Override
	@Async
	public Email sendEmailAsync(Email email) throws UnirestException {
		return this.sendEmail(email);
	}

	@Override
	@Async
	public SMS sendSMSAsync(SMS sms) throws UnirestException {
		return this.sendSMS(sms);
	}

	@Override
	@Async
	public Message notifySlackAsync(Message msg) throws UnirestException {
		return this.notifySlack(msg);
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws UnirestException {
		return null;
	}

	@Override
	public <T> Map<String, T> getMap(String url) throws UnirestException {
		return null;
	}

}
