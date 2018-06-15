package com.amx.jax.postman.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;

@Component
public class PostManServiceImpl implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManServiceImpl.class);

	@Autowired
	private SupportService supportService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SMService smsService;

	@Autowired
	private SlackService slackService;

	@Autowired
	private FileService fileService;

	@Autowired
	private AppConfig appConfig;

	@Override
	public Email sendEmail(Email email) throws PostManException {
		return emailService.sendEmail(supportService.filterMessageType(email));
	}

	@Override
	public File processTemplate(File file) {
		return fileService.create(file);
	}

	public File processTemplate(Templates template, Map<String, Object> map, Type fileType) {
		File file = new File();
		file.setTemplate(template);
		file.setType(fileType);
		file.setModel(map);
		return this.processTemplate(file);
	}

	@Override
	public SMS sendSMS(SMS sms) throws PostManException {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}
		this.smsService.sendSMS(sms);

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:END", "sendSMS");
		}
		return sms;
	}

	@Override
	@Async(ExecutorConfig.EXECUTER_BRONZE)
	public Notipy notifySlack(Notipy msg) throws PostManException {
		try {
			return slackService.sendNotification(msg);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public ExceptionReport notifyException(ExceptionReport e) {
		return this.notifyException(appConfig.getAppName(), e.getTitle(), e.getException(), e);
	}

	public ExceptionReport notifyException(String appname, String title, String exception, ExceptionReport e) {
		return slackService.sendException(appname, title, exception, e);
	}

	@Override
	public ExceptionReport notifyException(String title, Exception exc) {
		return this.notifyException(title, new ExceptionReport(exc));
	}

	@Override
	@Async(ExecutorConfig.EXECUTER_GOLD)
	public Email sendEmailAsync(Email email) throws PostManException {
		return this.sendEmail(email);
	}

	@Override
	@Async(ExecutorConfig.EXECUTER_PLATINUM)
	public SMS sendSMSAsync(SMS sms) throws PostManException {
		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMSAsync");
		}
		return this.sendSMS(sms);
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws PostManException {
		return null;
	}

	@Override
	public Map<String, Object> getMap(String url) throws PostManException {
		return null;
	}

	@Override
	@Async(ExecutorConfig.EXECUTER_BRONZE)
	public Email sendEmailToSupprt(SupportEmail supportEmail) throws PostManException {
		Email email = this.sendEmail(supportService.createContactUsEmail(supportEmail));
		Notipy msg = new Notipy();
		msg.setMessage(supportEmail.getSubject());
		msg.addLine("Tenant : " + AppContextUtil.getTenant());
		msg.addLine("VisitorName : " + supportEmail.getVisitorName());
		msg.addLine("VisitorEmail : " + supportEmail.getVisitorEmail());
		msg.addLine("VisitorPhone : " + supportEmail.getVisitorPhone());
		msg.addLine("VisitorMessage : " + supportEmail.getVisitorMessage());
		msg.setSubject(supportEmail.getSubject());
		msg.setChannel(Notipy.Channel.INQUIRY);
		this.notifySlack(msg);
		return email;
	}


}
