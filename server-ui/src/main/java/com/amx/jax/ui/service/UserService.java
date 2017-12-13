package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.jax.postman.Email;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.Templates;
import com.amx.jax.postman.client.PostManClient;
import com.mashape.unirest.http.exceptions.UnirestException;

@Service
public class UserService {

	@Autowired
	private PostManClient postManClient;

	public void notifyResetOTP(CivilIdOtpModel model) throws UnirestException {

		SMS sms = new SMS();
		sms.setTo("7710072192");
		sms.setText("Your OTP for Reset is " + model.getOtp());
		postManClient.sendSMS(sms);

		Email email = new Email();
		email.setTo("umesh.gupta@almullagroup.com");
		email.setTemplate(Templates.RESET_OTP);
		email.isHtml();

		postManClient.sendEmail(email);

	}

}
