package com.amx.jax.rbaac.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.OtpData;
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.FSEmployee;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.models.EmployeeInfo;
import com.amx.jax.rbaac.service.AuthNotificationService;
import com.amx.jax.rbaac.trnx.AuthLoginTrnxModel;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.Random;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthLoginOTPManager {

	public static final Logger logger = LoggerFactory.getLogger(AuthLoginOTPManager.class);

	@Autowired
	JaxUtil util;

	@Autowired
	JaxUtil jaxUtil;

	@Autowired
	AuthNotificationService authNotificationService;

	@Autowired
	AuthLoginManager authLoginManager;

	@Autowired
	CryptoUtil cryptoUtil;

	@Autowired
	RbaacDao rbaacDao;

	// Initiate new OTP
	public void init() {
		authLoginManager.init();
	}

	/**
	 * sends the otp to staff
	 */
	public void sendOtpStaff() {
		AuthLoginTrnxModel model = authLoginManager.get();
		FSEmployee employeeDetail = model.getEmpDetails();
		OtpData otpData = model.getOtpData();
		EmployeeInfo einfo = new EmployeeInfo();
		jaxUtil.convert(employeeDetail, einfo);
		authNotificationService.sendOtpSms(einfo, otpData);
	}

	/**
	 * generates otp to staff
	 */
	public SendOtpModel generateOtpTokensStaff(String ecno) {
		SendOtpModel sendOtpModel = new SendOtpModel();
		OtpData otpData = authLoginManager.get().getOtpData();
		if (otpData == null) {
			logger.info("otp data not found in trnx, creating new one");
			otpData = new OtpData();
		}

		String mOtp = util.createRandomPassword(6);

		sendOtpModel.setmOtp(mOtp);
		sendOtpModel.setmOtpPrefix(Random.randomAlpha(3));
		String hashedmOtp = cryptoUtil.getHash(ecno, sendOtpModel.getmOtpPrefix() + mOtp);
		otpData.setmOtp(mOtp);
		otpData.setHashedmOtp(hashedmOtp);
		otpData.setmOtpPrefix(sendOtpModel.getmOtpPrefix());

		// Save OTP to Cache
		authLoginManager.saveOtpData(otpData);
		return sendOtpModel;
	}

	/**
	 * Validates the otp for staff
	 */
	public AuthLoginTrnxModel validateOtpStaff(FSEmployee empDetails, String mOtp) {
		AuthLoginTrnxModel authLoginTrnxModel = null;
		OtpData otpData = authLoginManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(mOtp)) {
				throw new AuthServiceException(RbaacServiceError.MISSING_OTP, "Otp field is required");
			}
			// call from parameter master
			if (otpData.getValidateOtpAttempts() >= 3) {
				throw new AuthServiceException(
						RbaacServiceError.OTP_LIMIT_EXCEEDED,
						"Sorry, you cannot proceed to login. Please contact head office to unlock account");
			}
			// actual validation logic
			if (!otpData.getmOtp().equals(mOtp)) {
				otpData.setValidateOtpAttempts(otpData.getValidateOtpAttempts() + 1);
				throw new AuthServiceException(RbaacServiceError.INVALID_OTP, "Invalid otp");
			}
			otpData.setOtpValidated(true);
			UserRoleMaster usermaster = rbaacDao.fetchUserMasterDetails(empDetails.getEmployeeId());
			if (usermaster != null && usermaster.getUserRoleId() != null) {
				List<RoleDefinition> roleDef = rbaacDao.fetchEmpRoleMenu(usermaster.getUserRoleId());
				if (roleDef != null && roleDef.size() != 0) {
					authLoginTrnxModel = authLoginManager.fetchEmployeeDetails(empDetails, usermaster, roleDef);
				} else {
					throw new AuthServiceException(RbaacServiceError.INVALID_ROLE_DEFINITION,
							"Sorry, you cannot proceed to login. Please contact head office");
				}
			} else {
				throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS,
						"Sorry, you cannot proceed to login. Please contact head office");
			}
		} finally {
			authLoginManager.saveOtpData(otpData);
		}

		return authLoginTrnxModel;
	}

}
