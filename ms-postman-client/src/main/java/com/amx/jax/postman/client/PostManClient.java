package com.amx.jax.postman.client;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManClient implements PostManService {

	{
		Unirest.setObjectMapper(new ObjectMapper() {

			private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

			public <T> T readValue(String value, Class<T> valueType) {
				try {
					return jacksonObjectMapper.readValue(value, valueType);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}

			public String writeValue(Object value) {
				try {
					String resp = jacksonObjectMapper.writeValueAsString(value);
					String resp2 = JsonUtil.getMapper().writeValueAsString(value);
					return resp;
				} catch (JsonProcessingException e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	@Autowired
	private HttpServletResponse response;

	@Value("${jax.postman.url}")
	private String postManUrl;

	public void downloadPDF(String template, Object data, String fileName)
			throws UnirestException, DocumentException, IOException {
		File file = this.processTemplate(template, data, fileName);
		file.donwload(response, true);
	}

	public void createPDF(String template, Object data) throws IOException, DocumentException, UnirestException {
		File file = this.processTemplate(template, data, null);
		file.donwload(response, false);
	}

	public File processTemplate(String template, Object data, String fileName) throws UnirestException {
		HttpResponse<File> response = Unirest.post(postManUrl + PostManUrls.PROCESS_TEMPLATE)
				//.header("content-type", "application/json")
				.header("accept", "application/json").field("template", template).field("data", JsonUtil.toJson(data))
				.field("fileName", fileName).asObject(File.class);
		return response.getBody();
	}

	public SMS sendSMS(SMS sms) throws UnirestException {
		HttpResponse<SMS> response = Unirest.post(postManUrl + PostManUrls.SEND_SMS)
				.header("content-type", "application/json").body(sms).asObject(SMS.class);
		return response.getBody();
	}

	public Email sendEmail(Email email) throws UnirestException {
		HttpResponse<Email> response = Unirest.post(postManUrl + PostManUrls.SEND_EMAIL)
				.header("content-type", "application/json").body(email).asObject(Email.class);
		return response.getBody();
	}

	public Message notifySlack(Message msg) throws UnirestException {
		HttpResponse<Message> response = Unirest.post(postManUrl + PostManUrls.NOTIFY_SLACK)
				.header("accept", "application/json").header("Content-Type", "application/json")

				.body(msg).asObject(Message.class);
		return response.getBody();
	}

}
