package com.amx.jax.postman;

import org.json.JSONObject;

import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Message;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;

public interface PostManService {

	public static final String PARAM_LANG = "lang";
	public static final String PARAM_ASYNC = "async";

	public Email sendEmail(Email email) throws PostManException;

	public SMS sendSMS(SMS sms) throws PostManException;

	public Notipy notifySlack(Notipy msg) throws PostManException;

	public Exception notifyException(String title, Exception e);

	public Email sendEmailAsync(Email email) throws PostManException;

	public SMS sendSMSAsync(SMS sms) throws PostManException;

	public File processTemplate(Templates template, Object data, File.Type fileType) throws PostManException;

	public Boolean verifyCaptcha(String responseKey, String remoteIP) throws PostManException;

	public JSONObject getMap(String url) throws PostManException;

}
