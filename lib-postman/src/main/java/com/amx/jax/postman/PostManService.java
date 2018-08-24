package com.amx.jax.postman;

import java.util.List;

import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;

public interface PostManService {

	public static final String PARAM_LANG = "lang";
	public static final String PARAM_ASYNC = "async";

	public Email sendEmail(Email email) throws PostManException;

	public Email sendEmailToSupprt(SupportEmail email) throws PostManException;

	public SMS sendSMS(SMS sms) throws PostManException;

	public Notipy notifySlack(Notipy msg) throws PostManException;

	public ExceptionReport notifyException(ExceptionReport e);

	public ExceptionReport notifyException(String title, Exception exc);

	public Email sendEmailAsync(Email email) throws PostManException;

	public SMS sendSMSAsync(SMS sms) throws PostManException;

	public File processTemplate(File file) throws PostManException;

	public PostManResponse sendEmailBulk(List<Email> emailList);

}
