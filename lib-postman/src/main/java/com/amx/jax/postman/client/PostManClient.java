package com.amx.jax.postman.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManResponse;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
public class PostManClient implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Value("${jax.postman.url}")
	private String postManUrl;

	public void setLang(String lang) {
		ContextUtil.map().put(PARAM_LANG, lang);
	}

	public String getLang() {
		return ArgUtil.parseAsString(ContextUtil.map().get(PARAM_LANG));
	}

	public SMS sendSMS(SMS sms, Boolean async) throws PostManException {
		LOGGER.info("Sending SMS to {} ", sms.getTo().get(0));

		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.SEND_SMS)
					.queryParam(PARAM_LANG, getLang()).queryParam(PARAM_ASYNC, async).post(new HttpEntity<SMS>(sms))
					.as(SMS.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public SMS sendSMS(SMS sms) throws PostManException {
		return sendSMS(sms, Boolean.FALSE);
	}

	@Override
	public SMS sendSMSAsync(SMS sms) throws PostManException {
		return sendSMS(sms, Boolean.TRUE);
	}

	public Email sendEmail(Email email, Boolean async) throws PostManException {
		LOGGER.info("Sending email to {} ", email.getTo().get(0));
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.SEND_EMAIL)
					.queryParam(PARAM_LANG, getLang()).queryParam(PARAM_ASYNC, async).post(new HttpEntity<Email>(email))
					.as(Email.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public Email sendEmail(Email email) throws PostManException {
		return sendEmail(email, Boolean.FALSE);
	}

	@Override
	public Email sendEmailAsync(Email email) throws PostManException {
		return sendEmail(email, Boolean.TRUE);
	}

	/*
	 * Sends Bulk email Async. Template is being picked up from the first email in
	 * the list (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.postman.PostManService#sendEmailBulkForTemplate(java.util.List)
	 */
	public PostManResponse sendEmailBulk(List<Email> emailList) {
		LOGGER.info("Sending bulk Email for Notification Service ");
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.SEND_EMAIL_BULK).post(emailList)
					.as(PostManResponse.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public Email sendEmailToSupprt(SupportEmail email) throws PostManException {
		LOGGER.info("Sending support email from {}", email.getVisitorName());
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.SEND_EMAIL_SUPPORT)
					.queryParam(PARAM_LANG, getLang()).post(new HttpEntity<SupportEmail>(email)).as(Email.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	@Async
	public Notipy notifySlack(Notipy msg) throws PostManException {
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_SLACK)
					.queryParam(PARAM_LANG, getLang()).post(new HttpEntity<Notipy>(msg)).as(Notipy.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	public File processTemplate(File file) throws PostManException {
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.PROCESS_TEMPLATE_FILE)
					.queryParam(PARAM_LANG, getLang()).contentTypeJson().acceptJson().post(file).as(File.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}

	}

	@Override
	@Async
	public ExceptionReport notifyException(ExceptionReport e) {
		LOGGER.info("Sending exception = {} : {}", e.getTitle(), e.getClass().getName());
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_SLACK_EXCEP_REPORT)
					.contentTypeJson().queryParam("appname", appConfig.getAppName()).queryParam("title", e.getTitle())
					.queryParam("exception", e.getException()).post(e).as(ExceptionReport.class);

		} catch (Exception e1) {
			LOGGER.error("Exception while sending title={}", e.getTitle(), e1);
		}
		return e;
	}

	@Override
	@Async
	public ExceptionReport notifyException(String title, Exception exc) {
		return this.notifyException(new ExceptionReport(title, exc));
	}

}
