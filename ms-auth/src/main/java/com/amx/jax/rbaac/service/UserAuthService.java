/**
 * 
 */
package com.amx.jax.rbaac.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.dao.LoginDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dto.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.error.AuthServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.UserOtpManager;
import com.amx.jax.rbaac.trnx.UserOtpData;
import com.amx.utils.Random;

/**
 * The Class UserAuthService.
 *
 * @author abhijeet
 */
@Service
public class UserAuthService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceImpl.class);

	/** The login dao. */
	@Autowired
	LoginDao loginDao;

	/** The user otp data. */
	@Autowired
	UserOtpData userOtpData;

	/** The user otp manager. */
	@Autowired
	UserOtpManager userOtpManager;

	/** The Constant MAX_LOCK_COUNT. */
	private static final int MAX_LOCK_COUNT = 3;

	/**
	 * Verify user details.
	 * 
	 * @param empCode
	 *            the emp code
	 * @param identity
	 *            the identity
	 * @param ipAddress
	 *            the ip address
	 * @return the user auth init response DTO
	 * 
	 * @flow: -> Get Employee ||->-> Multiple Employees -> Error ||->-> Employee Not
	 *        valid -> Error ||->-> Employee Not Active or Deleted -> Error ||->->
	 *        Employee A/C Locked -> Error
	 * 
	 *        ||->->-> Proceed For OTP - Init Auth
	 */
	public UserAuthInitResponseDTO verifyUserDetails(String empCode, String identity, String ipAddress) {

		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(empCode) || StringUtils.isBlank(identity) || StringUtils.isBlank(ipAddress)) {
			throw new AuthServiceException("Employee Number, Civil Id & IP Address are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}

		List<Employee> employees = loginDao.getEmployees(empCode, identity, ipAddress);

		/**
		 * Invalid Employee Details
		 */
		if (null == employees || employees.isEmpty()) {
			throw new AuthServiceException("Employee Details not available", AuthServiceError.INVALID_USER_DETAILS);
		}

		/**
		 * Check for Multiple Employees
		 */
		if (employees.size() > 1) {
			throw new AuthServiceException("Multiple Users Corresponding to the same Info: Pls contact Support",
					AuthServiceError.MULTIPLE_USERS);
		}

		Employee emp = employees.get(0);

		/**
		 * Check if User is Active
		 */
		if (StringUtils.isBlank(emp.getIsActive()) || !"Y".equalsIgnoreCase(emp.getIsActive())
				|| "D".equalsIgnoreCase(emp.getDeletedUser()) || "Y".equalsIgnoreCase(emp.getDeletedUser())) {
			throw new AuthServiceException("User Not Active Or Deleted: User Account is Suspended.",
					AuthServiceError.USER_NOT_ACTIVE_OR_DELETED);
		}

		/**
		 * Check if user A/C is Locked. lockcnt >= 3
		 */
		if (null != emp.getLockCount() && emp.getLockCount().intValue() >= MAX_LOCK_COUNT) {
			throw new AuthServiceException(
					"User Account Locked : User Account Login is Suspended, from: " + emp.getLockDate(),
					AuthServiceError.USER_ACCOUNT_LOCKED);
		}

		/**
		 * Begin Init Auth for All User validation is Completed.
		 */
		OtpData otpData = userOtpManager.generateOtpTokens();

		userOtpManager.sendOtpSms(emp, otpData);

		userOtpData.fastPut(emp.getEmployeeNumber(), otpData.getmOtp());

		UserAuthInitResponseDTO dto = new UserAuthInitResponseDTO();

		dto.setAuthTransactionId(Random.randomAlphaNumeric(10));
		dto.setmOtpPrefix(otpData.getmOtpPrefix());
		dto.setInitOtpTime(String.valueOf(otpData.getInitTime()));
		dto.setTtlOtp(String.valueOf(otpData.getTtl()));

		LOGGER.info("OTP generated for Employee No: " + emp.getEmployeeNumber());

		return dto;

	}

}
