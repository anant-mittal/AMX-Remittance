package com.amx.jax.postman.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.AppParam;
import com.amx.jax.api.AmxApiResponse;
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
import com.amx.jax.postman.model.TemplatesMX;

/**
 * The Class PostManServiceImpl.
 */
@Component
public class PostManServiceImpl implements PostManService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(PostManServiceImpl.class);

	/** The support service. */
	@Autowired
	private SupportService supportService;

	/** The email service. */
	@Autowired
	private EmailService emailService;

	/** The sms service. */
	@Autowired
	private SMService smsService;

	/** The slack service. */
	@Autowired
	private SlackService slackService;

	/** The file service. */
	@Autowired
	private FileService fileService;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendEmail(com.amx.jax.postman.model.Email)
	 */
	@Override
	public AmxApiResponse<Email, Object> sendEmail(Email email) throws PostManException {
		return AmxApiResponse.build(emailService.sendEmail(supportService.filterMessageType(email)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendEmailAsync(com.amx.jax.postman.model.
	 * Email)
	 */
	@Override

	public AmxApiResponse<Email, Object> sendEmailAsync(Email email) throws PostManException {
		return this.sendEmail(email);
	}

	@Override
	public AmxApiResponse<Email, Object> sendEmailBulk(List<Email> emailList) {
		for (Email email : emailList) {
			this.sendEmail(email);
		}
		return AmxApiResponse.buildList(emailList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#processTemplate(com.amx.jax.postman.model.
	 * File)
	 */
	@Override
	public AmxApiResponse<File, Object> processTemplate(File file) {
		return AmxApiResponse.build(fileService.create(file));
	}

	/**
	 * Process template.
	 *
	 * @param template the template
	 * @param map      the map
	 * @param fileType the file type
	 * @return the file
	 */
	public File processTemplate(TemplatesMX template, Map<String, Object> map, Type fileType) {
		File file = new File();
		file.setITemplate(template);
		file.setType(fileType);
		file.setModel(map);
		return this.processTemplate(file).getResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendSMS(com.amx.jax.postman.model.SMS)
	 */
	@Override
	public AmxApiResponse<SMS, Object> sendSMS(SMS sms) throws PostManException {

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMS");
		}
		this.smsService.sendSMS(sms);

		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:END", "sendSMS");
		}
		return AmxApiResponse.build(sms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#notifySlack(com.amx.jax.postman.model.
	 * Notipy)
	 */
	@Override
	@Async(ExecutorConfig.EXECUTER_BRONZE)
	public AmxApiResponse<Notipy, Object> notifySlack(Notipy msg) throws PostManException {
		try {
			return AmxApiResponse.build(slackService.sendNotification(msg));
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#notifyException(com.amx.jax.postman.model.
	 * ExceptionReport)
	 */
	@Override
	public AmxApiResponse<ExceptionReport, Object> notifyException(ExceptionReport e) {
		return this.notifyException(appConfig.getAppName(), e.getTitle(), e.getException(), e);
	}

	/**
	 * Notify exception.
	 *
	 * @param appname   the appname
	 * @param title     the title
	 * @param exception the exception
	 * @param e         the e
	 * @return the exception report
	 */
	public AmxApiResponse<ExceptionReport, Object> notifyException(String appname, String title, String exception,
			ExceptionReport e) {
		return AmxApiResponse.build(slackService.sendException(appname, title, exception, e));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.postman.PostManService#notifyException(java.lang.String,
	 * java.lang.Exception)
	 */
	@Override
	public AmxApiResponse<ExceptionReport, Object> notifyException(String title, Exception exc) {
		return this.notifyException(new ExceptionReport(title, exc));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendSMSAsync(com.amx.jax.postman.model.
	 * SMS)
	 */
	@Override
	@Async(ExecutorConfig.EXECUTER_PLATINUM)
	public AmxApiResponse<SMS, Object> sendSMSAsync(SMS sms) throws PostManException {
		if (AppParam.DEBUG_INFO.isEnabled()) {
			LOGGER.info("{}:START", "sendSMSAsync");
		}
		return this.sendSMS(sms);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendEmailToSupprt(com.amx.jax.postman.
	 * model.SupportEmail)
	 */
	@Override
	@Async(ExecutorConfig.EXECUTER_BRONZE)
	public AmxApiResponse<Email, Object> sendEmailToSupprt(SupportEmail supportEmail) throws PostManException {
		Email email = this.sendEmail(supportService.createContactUsEmail(supportEmail)).getResult();
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
		return AmxApiResponse.build(email);
	}

}
