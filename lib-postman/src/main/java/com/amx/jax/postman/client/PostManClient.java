package com.amx.jax.postman.client;

import java.io.IOException;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
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
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManClient implements PostManService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostManClient.class);

	{
		Unirest.setObjectMapper(new ObjectMapper() {

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return JsonUtil.getMapper().readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					return JsonUtil.getMapper().writeValueAsString(value);
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

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
			HttpResponse<SMS> response = Unirest.post(postManUrl + PostManUrls.SEND_SMS)
					.queryString(PARAM_LANG, getLang()).queryString(PARAM_ASYNC, async)
					.header("content-type", "application/json").headers(appheader()).body(sms).asObject(SMS.class);
			return response.getBody();
		} catch (UnirestException e) {
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

	private Map<String, String> appheader() {
		return AppContextUtil.header();
	}

	public Email sendEmail(Email email, Boolean async) throws PostManException {
		LOGGER.info("Sending email to {} ", email.getTo().get(0));
		try {
			HttpResponse<Email> response = Unirest.post(postManUrl + PostManUrls.SEND_EMAIL)
					.queryString(PARAM_LANG, getLang()).queryString(PARAM_ASYNC, async)
					.header("content-type", "application/json").headers(appheader()).body(email).asObject(Email.class);
			return response.getBody();
		} catch (UnirestException e) {
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
			HttpResponse<Email> response = Unirest.post(postManUrl + PostManUrls.SEND_EMAIL_SUPPORT)
					.queryString(PARAM_LANG, getLang()).header("content-type", "application/json").headers(appheader())
					.body(email).asObject(Email.class);
			return response.getBody();
		} catch (UnirestException e) {
			throw new PostManException(e);
		}
	}

	@Override
	@Async
	public Notipy notifySlack(Notipy msg) throws PostManException {
		try {
			HttpResponse<Notipy> response = Unirest.post(postManUrl + PostManUrls.NOTIFY_SLACK)
					.queryString(PARAM_LANG, getLang()).header("accept", "application/json")
					.header("Content-Type", "application/json").headers(appheader())

					.body(msg).asObject(Notipy.class);
			return response.getBody();
		} catch (UnirestException e) {
			throw new PostManException(e);
		}
	}

	@Override
	public File processTemplate(Templates template, Object data, Type fileType) throws PostManException {

		try {
			HttpResponse<File> response = Unirest.post(postManUrl + PostManUrls.PROCESS_TEMPLATE)
					.queryString(PARAM_LANG, getLang())
					// .header("content-type", "application/json")
					.header("accept", "application/json").headers(appheader()).field("template", template)
					.field("data", JsonUtil.toJson(data)).field("fileType", fileType).asObject(File.class);
			return response.getBody();
		} catch (UnirestException e) {
			throw new PostManException(e);
		}
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws PostManException {
		// TODO Auto-generated method stub
		try {
			HttpResponse<JsonNode> response = Unirest.post("https://www.google.com/recaptcha/api/siteverify")
					// .header("content-type", "application/json")
					.header("accept", "application/json").field("secret", googleSecret).field("response", responseKey)
					.field("remoteip", remoteIP).asJson();
			if (response != null) {
				return response.getBody().getObject().getBoolean("success");
			}
			return false;
		} catch (UnirestException e) {
			throw new PostManException(e);
		}

	}

	@Override
	public JSONObject getMap(String url) throws PostManException {
		try {
			HttpResponse<JsonNode> response = Unirest.get(url)
					// .header("content-type", "application/json")
					.header("accept", "application/json").asJson();
			if (response != null) {
				return response.getBody().getObject();
			}
			return null;
		} catch (UnirestException e) {
			throw new PostManException(e);
		}
	}

	@Override
	@Async
	public Exception notifyException(String title, Exception e) {
		LOGGER.info("Sending exception = {} ", title);
		try {
			HttpResponse<Exception> response = Unirest.post(postManUrl + PostManUrls.NOTIFY_SLACK_EXCEP)
					.header("content-type", "application/json").headers(appheader()).queryString("title", title).body(e)
					.asObject(Exception.class);
		} catch (UnirestException e1) {
			LOGGER.error("title", e1);
		}
		return e;
	}

}
