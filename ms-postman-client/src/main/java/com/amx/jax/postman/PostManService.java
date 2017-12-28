package com.amx.jax.postman;

import java.io.IOException;

import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.lowagie.text.DocumentException;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface PostManService {

	public Email sendEmail(Email email) throws UnirestException;

	public File processTemplate(String template, Object data, String fileName) throws UnirestException;

	public SMS sendSMS(SMS sms) throws UnirestException;

	public Message notifySlack(Message msg) throws UnirestException;

	public void downloadPDF(String template, Object data, String fileName)
			throws IOException, DocumentException, UnirestException;

	void createPDF(String template, Object data) throws IOException, DocumentException, UnirestException;

}
