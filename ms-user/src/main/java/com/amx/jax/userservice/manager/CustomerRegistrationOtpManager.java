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

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.trnx.model.OtpData;
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

	}

	public SendOtpModel generateOtpTokens(String userId) {
		SendOtpModel sendOtpModel = new SendOtpModel();
		OtpData otpData = customerRegistrationManager.get().getOtpData();
		String eOtp = util.createRandomPassword(6);
		String hashedeOtp = cryptoUtil.getHash(userId, eOtp);
		sendOtpModel.seteOtpPrefix(Random.randomAlpha(3));
		otpData.seteOtp(eOtp);
		otpData.setHashedeOtp(hashedeOtp);
		otpData.seteOtpPrefix(sendOtpModel.geteOtpPrefix());

		String mOtp = util.createRandomPassword(6);
		String hashedmOtp = cryptoUtil.getHash(userId, eOtp);
		sendOtpModel.setmOtpPrefix(Random.randomAlpha(3));
		otpData.setmOtp(mOtp);
		otpData.setHashedmOtp(hashedmOtp);
		otpData.setmOtpPrefix(sendOtpModel.getmOtpPrefix());
		logger.info("generated new otps : {}", otpData.toString());
		customerRegistrationManager.saveOtpData(otpData);
		return sendOtpModel;
	}

	public void validateOtp(String mOtp, String eOtp) {

		OtpData otpData = customerRegistrationManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(eOtp) || StringUtils.isBlank(mOtp)) {
				throw new GlobalException("Otp field is required", JaxError.MISSING_OTP);
			}
			resetAttempts(otpData);
			if (otpData.getValidateOtpAttempts() >= otpSettings.getMaxValidateOtpAttempts()) {
				throw new GlobalException("Sorry, you cannot proceed to register. Please try to register after 12",
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED);
			}
			// actual validation logic
			if (!otpData.geteOtp().equals(eOtp) || !otpData.getmOtp().equals(mOtp)) {
				otpData.setValidateOtpAttempts(otpData.getValidateOtpAttempts() + 1);
				throw new GlobalException("Invalid otp", JaxError.INVALID_OTP);
			}
			otpData.setOtpValidated(true);
		} finally {
			customerRegistrationManager.saveOtpData(otpData);
		}
	}

	private void resetAttempts(OtpData otpData) {
		Date midnightToday = dateUtil.getMidnightToday();

		if (otpData.getLockDate() != null && midnightToday.compareTo(otpData.getLockDate()) > 0) {
			otpData.setLockDate(null);
			otpData.setValidateOtpAttempts(0);
		}
	}

}
