package com.amx.jax.postman;

import java.util.List;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.ExceptionReport;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.SupportEmail;

public interface PostManService {

	public static final String PARAM_LANG = "lang";
	public static final String PARAM_ASYNC = "async";

	public AmxApiResponse<Email, Object> sendEmail(Email email) throws PostManException;

	public AmxApiResponse<Email, Object> sendEmailToSupprt(SupportEmail email) throws PostManException;

	public AmxApiResponse<SMS, Object> sendSMS(SMS sms) throws PostManException;

	public AmxApiResponse<Notipy, Object> notifySlack(Notipy msg) throws PostManException;

	public AmxApiResponse<ExceptionReport, Object> notifyException(ExceptionReport e);

	public AmxApiResponse<ExceptionReport, Object> notifyException(String title, Exception exc);

	public AmxApiResponse<Email, Object> sendEmailAsync(Email email) throws PostManException;

	public AmxApiResponse<SMS, Object> sendSMSAsync(SMS sms) throws PostManException;

	public AmxApiResponse<File, Object> processTemplate(File file) throws PostManException;

	public AmxApiResponse<Email, Object> sendEmailBulk(List<Email> emailList);

}
