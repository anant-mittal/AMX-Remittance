package com.amx.jax.postman.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.rest.RestService;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;

@Component
public class PostManClient implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Value("${jax.postman.url}")
	private String postManUrl;

	private String googleSecret = "6LdtFEMUAAAAAKAhPVOk7iOA8SPnaOLGV9lFIqMJ";

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
					.queryParam(PARAM_LANG, getLang()).header("content-type", "application/json")
					.header("accept", "application/json").post(file).as(File.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public File processTemplate(Templates template, Object data, Type fileType) throws PostManException {
		File file = new File();
		file.setTemplate(template);
		file.setType(fileType);
		// file.setObject(data);
		file.setModel(JsonUtil.fromJson(JsonUtil.toJson(data), Map.class));
		return this.processTemplate(file);
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws PostManException {
		// TODO Auto-generated method stub
		try {
			@SuppressWarnings("unchecked")
			Map<String, Object> resp = restService.ajax("https://www.google.com/recaptcha/api/siteverify")
					.header("accept", "application/json").field("secret", googleSecret).field("response", responseKey)
					.field("remoteip", remoteIP).postForm().as(Map.class);
			if (resp != null) {
				return ArgUtil.parseAsBoolean(resp.get("success"));
			}
			return null;
		} catch (Exception e) {
			throw new PostManException(e);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMap(String url) throws PostManException {
		try {
			Map<String, Object> response = restService.ajax(url)
					// .header("content-type", "application/json")
					.header("accept", "application/json").get().as(Map.class);
			if (response != null) {
				return response;
			}
			return null;
		} catch (Exception e) {
			throw new PostManException(e);
		}
	}

	@Override
	@Async
	public Exception notifyException(String title, Exception e) {
		LOGGER.info("Sending exception = {} ", title);

		Exception simpleE = new Exception(e);

		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.NOTIFY_SLACK_EXCEP)
					.header("content-type", "application/json").queryParam("appname", appConfig.getAppName())
					.queryParam("title", title).post(simpleE).as(Exception.class);
		} catch (Exception e1) {
			LOGGER.error("Exception while sending title={}", title, e1);
		}
		return e;
	}

}
