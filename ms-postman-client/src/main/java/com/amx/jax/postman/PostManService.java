package com.amx.jax.postman;

import java.util.Map;

import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface PostManService {

	public Email sendEmail(Email email) throws UnirestException;

	public SMS sendSMS(SMS sms) throws UnirestException;

	public Message notifySlack(Message msg) throws UnirestException;

	public Email sendEmailAsync(Email email) throws UnirestException;

	public SMS sendSMSAsync(SMS sms) throws UnirestException;

	public Message notifySlackAsync(Message msg) throws UnirestException;

	public File processTemplate(Templates template, Object data, File.Type fileType) throws UnirestException;

	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws UnirestException;

	public <T> Map<String, T> getMap(String url) throws UnirestException;

}
