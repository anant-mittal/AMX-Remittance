package com.amx.jax.auth.manager;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.SendOtpModel;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.auth.dao.LoginDao;
import com.amx.jax.auth.dbmodel.Employee;
import com.amx.jax.auth.dbmodel.RoleDefinition;
import com.amx.jax.auth.dbmodel.UserRoleMaster;
import com.amx.jax.auth.error.JaxError;
import com.amx.jax.auth.exception.GlobalException;
import com.amx.jax.auth.models.EmployeeInfo;
import com.amx.jax.auth.service.JaxNotificationService;
import com.amx.jax.auth.trnx.AuthLoginTrnxModel;
import com.amx.jax.trnx.model.OtpData;
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
	JaxNotificationService jaxNotificationService;
	
	@Autowired
	AuthLoginManager authLoginManager;
	
	@Autowired
	CryptoUtil cryptoUtil;
	
	@Autowired
	OtpSettings otpSettings;
	
	@Autowired
	LoginDao loginDao;
	
	// Initiate new OTP
	public void init() {
		authLoginManager.init();
	}
	
	/**
	 * sends the otp to staff
	 */
	public void sendOtpStaff() {
		AuthLoginTrnxModel model = authLoginManager.get();
		Employee employeeDetail = model.getEmpDetails();
		CivilIdOtpModel civilIdOtpModel = new CivilIdOtpModel();
		OtpData otpData = model.getOtpData();
		EmployeeInfo einfo = new EmployeeInfo();
		jaxUtil.convert(employeeDetail, einfo);
		jaxUtil.convert(otpData,civilIdOtpModel);
		jaxNotificationService.sendOtpSms(einfo, civilIdOtpModel);
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
		String hashedmOtp = cryptoUtil.getHash(ecno, mOtp);
		sendOtpModel.setmOtp(mOtp);
		sendOtpModel.setmOtpPrefix(Random.randomAlpha(3));
		otpData.setmOtp(mOtp);
		otpData.setHashedmOtp(hashedmOtp);
		otpData.setmOtpPrefix(sendOtpModel.getmOtpPrefix());
		logger.info("generated new otps : {}", otpData.toString());
		authLoginManager.saveOtpData(otpData);
		return sendOtpModel;
	}
	
	/**
	 * Validates the otp for staff
	 */
	public AuthLoginTrnxModel validateOtpStaff(Employee empDetails,String mOtp) {
		AuthLoginTrnxModel authLoginTrnxModel = null;
		OtpData otpData = authLoginManager.get().getOtpData();
		try {
			if (StringUtils.isBlank(mOtp)) {
				throw new GlobalException("Otp field is required", JaxError.MISSING_OTP);
			}
			// call from parameter master
			if (otpData.getValidateOtpAttempts() >= 3) {
				throw new GlobalException("Sorry, you cannot proceed to login. Please contact head office to unlock account",
						JaxError.VALIDATE_OTP_LIMIT_EXCEEDED);
			}
			// actual validation logic
			if (!otpData.getmOtp().equals(mOtp)) {
				otpData.setValidateOtpAttempts(otpData.getValidateOtpAttempts() + 1);
				throw new GlobalException("Invalid otp", JaxError.INVALID_OTP);
			}
			otpData.setOtpValidated(true);
			UserRoleMaster usermaster = loginDao.fetchUserMasterDetails(empDetails.getEmployeeId());
			if(usermaster != null && usermaster.getUserRoleId() != null){
				List<RoleDefinition> roleDef = loginDao.fetchEmpRoleMenu(usermaster.getUserRoleId());
				if(roleDef != null && roleDef.size() != 0){
					authLoginTrnxModel = authLoginManager.fetchEmployeeDetails(empDetails, usermaster, roleDef);
				}else{
					throw new GlobalException("Sorry, you cannot proceed to login. Please contact head office",
							JaxError.INVALID_ROLE_DEFINITION);
				}
			}else{
				throw new GlobalException("Sorry, you cannot proceed to login. Please contact head office",
						JaxError.INVALID_EMPLOYEE_DETAILS);
			}
		} finally {
			authLoginManager.saveOtpData(otpData);
		}
		
		return authLoginTrnxModel;
	}

}
