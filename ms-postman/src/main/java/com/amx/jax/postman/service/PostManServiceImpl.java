package com.amx.jax.postman.service;

import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.JsonUtil;

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
	private TemplateService templateService;

	private String tnt = null;
	private int counter = 0;

	public void print() {
		if (tnt == null) {
			tnt = TenantContextHolder.currentSite().toString();
			LOGGER.info("Settin new {}", tnt);
		}
		LOGGER.info("print {} {}", tnt, counter++);
	}

	@Override
	public Email sendEmail(Email email) throws PostManException {
		String to = null;
		try {
			to = email.getTo().get(0);
			if (email.getTemplate() != null) {
				File file = new File();
				file.setTemplate(email.getTemplate());
				file.setModel(email.getModel());
				file.setLang(email.getLang());
				email.setMessage(this.processTemplate(file).getContent());
			}

			if (email.getFiles() != null && email.getFiles().size() > 0) {
				for (File file : email.getFiles()) {
					if (file.getLang() == null) {
						file.setLang(email.getLang());
					}
					fileService.create(file);
				}
			}
			emailService.send(email);
			LOGGER.info("Email sent to {} = {} ", to, email.getTemplate());
		} catch (Exception e) {
			this.notifyException(to, e);
		}
		return email;
	}

	@Override
	public File processTemplate(Templates template, Object data, Type fileType) throws PostManException {
		Map<String, Object> map = JsonUtil.toMap(data);
		return this.processTemplate(template, map, fileType);
	}

	@Override
	public File processTemplate(File file) {
		try {
			fileService.create(file);
		} catch (Exception e) {
			this.notifyException(file.getTemplate().toString(), e);
		}
		LOGGER.info("Template Generated = {}", file.getTemplate());
		return file;
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
		String to = null;
		try {
			to = sms.getTo().get(0);
			if (sms.getTemplate() != null) {
				Context context = new Context(new Locale(sms.getLang().toString()));
				context.setVariables(sms.getModel());
				sms.setMessage(templateService.processHtml(sms.getTemplate(), context));
			}
			this.smsService.sendSMS(sms);
			LOGGER.info("Email sent to {} = {} ", to, sms.getTemplate());
		} catch (Exception e) {
			this.notifyException(to, e);
		}
		return sms;
	}

	@Override
	@Async
	public Notipy notifySlack(Notipy msg) throws PostManException {
		try {
			return slackService.sendNotification(msg);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	@Async
	public Exception notifyException(String title, Exception e) {
		return slackService.sendException(title, e);
	}

	@Override
	@Async
	public Email sendEmailAsync(Email email) throws PostManException {
		return this.sendEmail(email);
	}

	@Override
	@Async
	public SMS sendSMSAsync(SMS sms) throws PostManException {
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
	@Async
	public Email sendEmailToSupprt(SupportEmail supportEmail) throws PostManException {
		Email email = this.sendEmail(supportService.createContactUsEmail(supportEmail));
		Notipy msg = new Notipy();
		msg.setMessage(supportEmail.getSubject());
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
