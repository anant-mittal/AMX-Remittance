package com.amx.jax.ui.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.jax.postman.Email;
import com.amx.jax.postman.SMS;
import com.amx.jax.postman.Templates;
import com.amx.jax.postman.client.PostManClient;
import com.amx.jax.ui.Constants;

@Service
public class UserService {

	private Logger log = Logger.getLogger(UserService.class);

	@Autowired
	private PostManClient postManClient;

	@Async
	public void notifyResetOTP(CivilIdOtpModel model) {

		SMS sms = new SMS();

		try {
			sms.setTo("7710072192");
			sms.setText("Your OTP for Reset is " + model.getOtp());
			sms.setTemplate(Templates.RESET_OTP_SMS);
			sms.getModel().put("data", model);
			postManClient.sendSMS(sms);
		} catch (Exception e) {
			log.error("Error while sending OTP SMS to 7710072192", e);
		}

		Email email = new Email();
		email.setSubject("Verify Your Account");
		email.setFrom("amxjax@gmail.com");
		if (model.getEmail() != null && !Constants.EMPTY.equals(model.getEmail())) {
			email.setTo(model.getEmail());
		} else {
			email.setTo("riddhi.madhu@almullagroup.com");
		}
		email.setTemplate(Templates.RESET_OTP);
		email.setHtml(true);
		email.getModel().put("data", model);

		try {
			postManClient.sendEmail(email);
		} catch (Exception e) {
			log.error("Error while sending OTP Email to" + model.getEmail(), e);
		}

	}

}
