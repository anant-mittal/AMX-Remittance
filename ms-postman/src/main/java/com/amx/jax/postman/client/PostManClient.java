package com.amx.jax.postman.client;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.postman.File;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.api.PostManService;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

@Component
public class PostManClient {

	@Autowired
	private HttpServletResponse response;

	@Autowired
	private PostManService postManService;

	public void downloadPDF(String template, Object data, String fileName) throws IOException, DocumentException {
		File file = postManService.processTemplate(template, data, fileName);
		file.donwload(response);
	}

	public void sendSMS(SMS sms) throws UnirestException {
		postManService.sendSMS(sms);
	}

}
