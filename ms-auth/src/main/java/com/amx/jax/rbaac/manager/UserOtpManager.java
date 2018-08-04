/**
 * 
 */
package com.amx.jax.rbaac.manager;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.model.OtpData;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.Templates;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.utils.CryptoUtil;
import com.amx.utils.Random;

/**
 * The Class UserOtpManager.
 *
 * @author abhijeet
 */
@Component
public class UserOtpManager {

	/** The post man service. */
	@Autowired
	private PostManService postManService;

	/** The app config. */
	@Autowired
	private AppConfig appConfig;

	/** The logger. */
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

	/** The otp TTL. */
	private long otpTTL = 10 * 60 * 1000;

	/**
	 * Generate otp tokens.
	 *
	 * @return the otp data
	 */
	public OtpData generateOtpTokens() {

		OtpData otpData = new OtpData();

		otpData.setmOtp(Random.randomNumeric(6));
		otpData.setmOtpPrefix(Random.randomAlpha(3));

		try {
			otpData.setHashedmOtp(CryptoUtil.getSHA1Hash(otpData.getmOtp()));
		} catch (NoSuchAlgorithmException e) {
			// Hash Can Not be generated
			otpData.setHashedmOtp(null);
		}

		long initTime = System.currentTimeMillis();

		otpData.setInitTime(initTime);
		otpData.setTtl(initTime + otpTTL);

		return otpData;
	}

	/**
	 * Send to slack.
	 *
	 * @param channel
	 *            the channel
	 * @param to
	 *            the to
	 * @param prefix
	 *            the prefix
	 * @param otp
	 *            the otp
	 */
	public void sendToSlack(String channel, String to, String prefix, String otp) {
		Notipy msg = new Notipy();
		msg.setMessage(String.format("%s = %s", channel, to));
		msg.addLine(String.format("OTP = %s-%s", prefix, otp));
		msg.setChannel(Channel.NOTIPY);
		try {
			postManService.notifySlack(msg);
		} catch (PostManException e) {
			LOGGER.error("Error in SlackNotify", e);
		}
	}

	/**
	 * Send otp sms.
	 *
	 * @param einfo
	 *            the einfo
	 * @param model
	 *            the model
	 */
	// Employee otp to login: passing Employee for including any personal Msg
	public void sendOtpSms(Employee einfo, OtpData model) {

		LOGGER.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", einfo.getEmployeeName(),
				einfo.getTelephoneNumber()));

		SMS sms = new SMS();
		sms.addTo(einfo.getTelephoneNumber());
		sms.setModelData(model);
		sms.setTemplate(Templates.RESET_OTP_SMS);

		try {

			postManService.sendSMSAsync(sms);

			if (!appConfig.isProdMode()) {
				sendToSlack("mobile", sms.getTo().get(0), model.getmOtpPrefix(), model.getmOtp());
				sendToSlack("mobile", sms.getTo().get(0), "Otp-Hash", model.getHashedmOtp());
			}

		} catch (PostManException e) {
			LOGGER.error("error in sendOtpSms", e);
			throw new AuthServiceException(e);
		}
	}


}
