package com.amx.jax.postman.client;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.File.Type;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManClient implements PostManService {

	private Logger log = Logger.getLogger(getClass());

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

	@Async
	public SMS sendSMS(SMS sms) throws UnirestException {
		HttpResponse<SMS> response = Unirest.post(postManUrl + PostManUrls.SEND_SMS)
				.header("content-type", "application/json").body(sms).asObject(SMS.class);
		return response.getBody();
	}

	@Override
	@Async
	public SMS sendSMSAsync(SMS sms) throws UnirestException {
		return sendSMS(sms);
	}

	public Email sendEmail(Email email) throws UnirestException {
		HttpResponse<Email> response = Unirest.post(postManUrl + PostManUrls.SEND_EMAIL)
				.header("content-type", "application/json").body(email).asObject(Email.class);
		return response.getBody();
	}

	@Override
	@Async
	public Email sendEmailAsync(Email email) throws UnirestException {
		return sendEmail(email);
	}

	public Message notifySlack(Message msg) throws UnirestException {
		HttpResponse<Message> response = Unirest.post(postManUrl + PostManUrls.NOTIFY_SLACK)
				.header("accept", "application/json").header("Content-Type", "application/json")

				.body(msg).asObject(Message.class);
		return response.getBody();
	}

	@Override
	@Async
	public Message notifySlackAsync(Message msg) throws UnirestException {
		return notifySlack(msg);
	}

	@Override
	public File processTemplate(Templates template, Object data, Type fileType) throws UnirestException {
		HttpResponse<File> response = Unirest.post(postManUrl + PostManUrls.PROCESS_TEMPLATE)
				// .header("content-type", "application/json")
				.header("accept", "application/json").field("template", template).field("data", JsonUtil.toJson(data))
				.field("fileType", fileType).asObject(File.class);
		return response.getBody();
	}

	@Override
	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws UnirestException {
		// TODO Auto-generated method stub
		HttpResponse<JsonNode> response = Unirest.post("https://www.google.com/recaptcha/api/siteverify")
				// .header("content-type", "application/json")
				.header("accept", "application/json").field("secret", googleSecret).field("response", responseKey)
				.field("remoteip", remoteIP).asJson();

		if (response != null) {
			return response.getBody().getObject().getBoolean("success");
		}
		return false;
	}

}
