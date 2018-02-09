package com.amx.jax.postman.service;

import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
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

	@Autowired
	private HttpServletRequest request;

	@Value("${jax.lang.default}")
	private String defaultLang;

	@Autowired
	private LocaleResolver localeResolver;

	public Locale getLocale() {
		String lang = localeResolver.resolveLocale(request).toString();
		if (lang == null) {
			lang = "en_KW";
		}
		return new Locale(lang);
	}

	@Override
	public Email sendEmail(Email email) throws UnirestException {
		String to = null;
		try {
			to = email.getTo().get(0);
			if (email.getTemplate() != null) {
				Context context = new Context(getLocale());
				context.setVariables(email.getModel());
				email.setMessage(this.processTemplate(email.getTemplate(), email.getModel(), null).getContent());
			}

			if (email.getFiles() != null && email.getFiles().size() > 0) {
				for (File file : email.getFiles()) {
					fileService.create(file);
				}
			}
			emailService.send(email);
			LOGGER.info("Email sent to {}", to);
		} catch (Exception e) {
			this.notifyException(to, e);
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
		file.setTemplate(template);
		file.setType(fileType);
		file.setModel(map);

		try {
			return fileService.create(file);
		} catch (Exception e) {
			this.notifyException(template.toString(), e);
		}
		LOGGER.info("Template Generated sent to {}", template.toString());
		return file;
	}

	@Override
	public SMS sendSMS(SMS sms) throws UnirestException {
		String to = null;
		try {
			to = sms.getTo().get(0);
			if (sms.getTemplate() != null) {
				Context context = new Context(getLocale());
				context.setVariables(sms.getModel());
				sms.setMessage(templateService.processHtml(sms.getTemplate(), context));
			}
			this.smsService.sendSMS(sms);
			LOGGER.info("SMS sent to {}", to);
		} catch (Exception e) {
			this.notifyException(to, e);
		}
		return sms;
	}

	@Override
	public Message notifySlack(Message msg) throws UnirestException {
		return slackService.sendNotification(msg);
	}

	@Override
	@Async
	public Exception notifyException(String title, Exception e) {
		return slackService.sendException(title, e);
	}

	@Override
	@Async
	public Message notifySlackAsync(Message msg) throws UnirestException {
		return this.notifySlack(msg);
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
	public Exception notifyExceptionAsync(String title, Exception e) {
		return this.notifyException(title, e);
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws UnirestException {
		return null;
	}

	@Override
	public JSONObject getMap(String url) throws UnirestException {
		return null;
	}

}
