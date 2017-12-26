package com.amx.jax.postman.api;

import com.amx.jax.postman.Email;
import com.amx.jax.postman.File;
import com.amx.jax.postman.Message;
import com.amx.jax.postman.SMS;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface PostManService {

	public abstract void sendEmail(Email email);

	public File processTemplate(String template, Object data, String fileName);

	public void sendSMS(SMS sms) throws UnirestException;

	public void notifySlack(Message msg) throws UnirestException;

}
