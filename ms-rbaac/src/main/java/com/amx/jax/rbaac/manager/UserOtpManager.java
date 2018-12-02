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
import com.amx.jax.dict.UserClient;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.model.OtpData;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Notipy;
import com.amx.jax.postman.model.Notipy.Channel;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.utils.CryptoUtil;
import com.amx.utils.CryptoUtil.HashBuilder;
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

	public static String getOtpHash(String otp) {
		try {
			return CryptoUtil.getSHA1Hash(otp);
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/**
	 * 
	 * Generate otp tokens.
	 *
	 * @return the otp data
	 */
	public OtpData generateOtpTokens(String sac) {

		OtpData otpData = new OtpData();

		/**
		 * TODO:- Get Device RegId for {@link ClientType#NOTP_APP}
		 * 
		 * @author lalittanwar
		 */
		HashBuilder builder = new HashBuilder().interval(otpTTL).secret("DEVICEREGID").message(sac);
		otpData.setmOtpPrefix(sac);
		otpData.setmOtp(builder.toNumeric(6).output());

		otpData.setHashedmOtp(getOtpHash(otpData.getmOtp()));

		long initTime = System.currentTimeMillis();

		otpData.setInitTime(initTime);
		otpData.setTtl(initTime + otpTTL);

		return otpData;
	}

	/**
	 * Send to slack.
	 *
	 * @param channel the channel
	 * @param to      the to
	 * @param prefix  the prefix
	 * @param otp     the otp
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
	 * @param einfo the einfo
	 * @param model the model
	 */
	// Employee otp to login: passing Employee for including any personal Msg
	public void sendOtpSms(Employee einfo, OtpData model, String slackMsg) {

		LOGGER.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", einfo.getEmployeeName(),
				einfo.getTelephoneNumber()));

		SMS sms = new SMS();
		sms.addTo(einfo.getTelephoneNumber());
		sms.setModelData(model);
		sms.setITemplate(TemplatesMX.RESET_OTP_SMS);

		try {

			postManService.sendSMSAsync(sms);

			if (!appConfig.isProdMode()) {
				sendToSlack(slackMsg + " : " + "mobile", sms.getTo().get(0), model.getmOtpPrefix(), model.getmOtp());
				// sendToSlack("mobile", sms.getTo().get(0), "Otp-Hash", model.getHashedmOtp());
			}

		} catch (PostManException e) {
			LOGGER.error("error in sendOtpSms", e);
			throw new AuthServiceException(e);
		}
	}

}
