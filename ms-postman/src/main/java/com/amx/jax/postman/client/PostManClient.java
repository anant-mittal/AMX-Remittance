package com.amx.jax.postman.client;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.File;
import com.amx.jax.postman.Message;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.api.PostManService;
import com.amx.jax.postman.api.PostManServiceImpl;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManClient implements PostManService {

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private PostManServiceImpl postManService;

	public void downloadPDF(String template, Object data, String fileName) throws IOException, DocumentException {
		File file = postManService.processTemplate(template, data, fileName);
		file.donwload(response, true);
	}

	public void createPDF(String template, Object data) throws IOException, DocumentException {
		File file = postManService.processTemplate(template, data, null);
		file.donwload(response, false);
	}

	public File processTemplate(String template, Object data, String fileName) {
		File file = postManService.processTemplate(template, data, fileName);
		return file;// file.getContent();
	}

	public void sendSMS(SMS sms) throws UnirestException {
		postManService.sendSMS(sms);
	}

	public void sendEmail(Email email) {
		postManService.sendEmail(email);
	}

	@Override
	public void notifySlack(Message msg) throws UnirestException {
		postManService.notifySlack(msg);

	}

}
