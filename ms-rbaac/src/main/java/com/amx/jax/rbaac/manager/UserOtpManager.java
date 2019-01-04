/**
 * 
 */
package com.amx.jax.rbaac.manager;

import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AmxConstants;
import com.amx.jax.model.OtpData;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.utils.CryptoUtil;
import com.amx.utils.CryptoUtil.HashBuilder;

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

	/** The logger. */
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());

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
	public OtpData generateOtpTokens(String secret, String sac) {

		OtpData otpData = new OtpData();

		/**
		 * TODO:- Get Device RegId for {@link ClientType#NOTP_APP}
		 * 
		 * @author lalittanwar
		 */
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("SMS OTP TOKEN {} {} {}", AmxConstants.SMS_OTP_TTL, secret, sac);
		}

		HashBuilder builder = new HashBuilder().interval(AmxConstants.SMS_OTP_TTL).secret(secret).message(sac);
		otpData.setmOtpPrefix(sac);
		otpData.setmOtp(builder.toHMAC().toNumeric(AmxConstants.OTP_LENGTH).output());

		otpData.setHashedmOtp(getOtpHash(otpData.getmOtp()));

		long initTime = System.currentTimeMillis();

		otpData.setInitTime(initTime);
		otpData.setTtl(initTime + AmxConstants.SMS_OTP_TTL);

		return otpData;
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
	public void sendOtpSms(Employee einfo, OtpData model, String slackMsg) {

		LOGGER.info(String.format("Sending OTP SMS to customer :%s on mobile_no :%s  ", einfo.getEmployeeName(),
				einfo.getTelephoneNumber()));

		SMS sms = new SMS();
		sms.addTo(einfo.getTelephoneNumber());
		sms.setModelData(model);
		sms.setITemplate(TemplatesMX.RESET_OTP_SMS);

		try {
			postManService.sendSMSAsync(sms);
		} catch (PostManException e) {
			LOGGER.error("error in sendOtpSms", e);
			throw new AuthServiceException(e);
		}
	}

}
