package com.amx.jax.rbaac.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.AppConfig;
import com.amx.jax.auth.models.EmployeeInfo;
import com.amx.jax.model.OtpData;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthNotificationService {

	@Autowired
	private PostManService postManService;

	@Autowired
	private AppConfig appConfig;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	public void sendToSlack(String channel, String to, String prefix, String otp) {
		Notipy msg = new Notipy();
		msg.setMessage(String.format("%s = %s", channel, to));
		msg.addLine(String.format("OTP = %s-%s", prefix, otp));
		msg.setChannel(Channel.NOTIPY);
		try {
			postManService.notifySlack(msg);
		} catch (PostManException e) {
			logger.error("error in SlackNotify", e);
		}
	}

	// employee otp to login
	public void sendOtpSms(EmployeeInfo einfo, OtpData model) {

		logger.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", einfo.getEmployeeName(),
				einfo.getTelephoneNumber()));

		SMS sms = new SMS();
		sms.addTo(einfo.getTelephoneNumber());
		sms.setModelData(model);
		sms.setTemplate(Templates.RESET_OTP_SMS);

		try {

			postManService.sendSMSAsync(sms);
			if (!appConfig.isProdMode()) {
				sendToSlack("mobile", sms.getTo().get(0), model.getmOtpPrefix(), model.getmOtp());
			}

		} catch (PostManException e) {
			logger.error("error in sendOtpSms", e);
		}
	} // end of sendOtpSms
}
