package com.amx.jax.userservice.manager;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.service.CustomerRegistrationService;
import com.amx.jax.userservice.validation.CustomerPersonalDetailValidator;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.DateUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRegistrationOtpManager {

	public static final Logger logger = LoggerFactory.getLogger(CustomerRegistrationService.class);

	@Autowired
	JaxUtil util;

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	CustomerRegistrationManager customerRegistrationManager;

	@Autowired
	CustomerPersonalDetailValidator customerPersonalDetailValidator;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	OtpSettings otpSettings;

	@Autowired
	DateUtil dateUtil;

	/**
	 * sends the otp
	 */
	public void sendOtp() {
		CustomerRegistrationTrnxModel model = customerRegistrationManager.get();
		CustomerPersonalDetail customerPersonalDetail = model.getCustomerPersonalDetail();
		CivilIdOtpModel civilIdOtpModel = new CivilIdOtpModel();
		OtpData otpData = model.getOtpData();
		PersonInfo pinfo = new PersonInfo();
		jaxUtil.convert(customerPersonalDetail, civilIdOtpModel);
		jaxUtil.convert(customerPersonalDetail, pinfo);
		jaxUtil.convert(otpData, civilIdOtpModel);
		jaxNotificationService.sendOtpEmail(pinfo, civilIdOtpModel);
		jaxNotificationService.sendOtpSms(pinfo, civilIdOtpModel);
		otpData.incrementSentCount();
		if (otpData.getSendOtpAttempts() >= otpSettings.getMaxSendOtpAttempts()) {
			otpData.setLockDate(new Date());
		}
		customerRegistrationManager.saveOtpData(otpData);
	}

	/**
	 * generates otp
	 */
	public SendOtpModel generateOtpTokens(String userId) {
		SendOtpModel sendOtpModel = new SendOtpModel();
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		if (otpData == null) {
			logger.info("otp data not found in trnx, creating new one");
			otpData = new OtpData();
		}
		
		String emailId = customerRegistrationManager.get().getCustomerPersonalDetail().getEmail();
		if(!StringUtils.isBlank(emailId)) {
			String eOtp = Random.randomNumeric(6);
			String hashedeOtp = cryptoUtil.getHash(userId, eOtp);
			sendOtpModel.seteOtpPrefix(Random.randomAlpha(3));
			sendOtpModel.seteOtp(eOtp);
			otpData.seteOtp(eOtp);
			otpData.setHashedeOtp(hashedeOtp);
			otpData.seteOtpPrefix(sendOtpModel.geteOtpPrefix());
		}
		
		String mOtp = Random.randomNumeric(6);
		String hashedmOtp = cryptoUtil.getHash(userId, mOtp);
		sendOtpModel.setmOtp(mOtp);
		sendOtpModel.setmOtpPrefix(Random.randomAlpha(3));
		otpData.setmOtp(mOtp);
		otpData.setHashedmOtp(hashedmOtp);
		otpData.setmOtpPrefix(sendOtpModel.getmOtpPrefix());
		logger.info("generated new otps : {}", otpData.toString());
		customerRegistrationManager.saveOtpData(otpData);
		return sendOtpModel;
	}

	/**
	 * Validates the otp
	 */
	public void validateOtp(String mOtp, String eOtp) {

		OtpData otpData = customerRegistrationManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(eOtp) || StringUtils.isBlank(mOtp)) {
				throw new GlobalException(JaxError.MISSING_OTP, "Otp field is required");
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				throw new GlobalException(
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED,
						"Sorry, you cannot proceed to register. Please try to register after 12 midnight");
			}
			// actual validation logic
			if (!otpData.geteOtp().equals(eOtp) || !otpData.getmOtp().equals(mOtp)) {
				otpMismatch(otpData);
			}
			otpData.setOtpValidated(true);
			otpData.resetCounts();
		} finally {
			customerRegistrationManager.saveOtpData(otpData);
		}
	}

	/**
	 * called when otp is not matching
	 * 
	 * @param otpData
	 * 
	 */
	private void otpMismatch(OtpData otpData) {
		otpData.setValidateOtpAttempts(otpData.getValidateOtpAttempts() + 1);
		if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
			otpData.setLockDate(new Date());
		}
		throw new GlobalException(JaxError.INVALID_OTP, "Invalid otp");

	}

	/** resets attempts of otp */
	private void resetAttempts(OtpData otpData) {
		Date midnightToday = dateUtil.getMidnightToday();

		if (otpData.getLockDate() != null && midnightToday.compareTo(otpData.getLockDate()) > 0) {
			otpData.resetCounts();
		}
	}

}
