/**
 * 
 */
package com.amx.jax.rbaac.service;

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

import io.netty.util.internal.StringUtil;

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
	 */
	public UserAuthInitResponseDTO verifyUserDetails(String empCode, String identity, String ipAddress) {

		if (!StringUtil.isNullOrEmpty(empCode) && !StringUtil.isNullOrEmpty(identity)
				&& !StringUtil.isNullOrEmpty(ipAddress)) {

			Employee emp = validateEmployeeData(empCode, identity, ipAddress);

			if (emp != null) {

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

			} else {
				throw new AuthServiceException("Employee Details not available",
						AuthServiceError.INVALID_EMPLOYEE_DETAILS);
			}
		} else {
			throw new AuthServiceException("Employee Number, Civil Id & IP Address are Manadatory",
					AuthServiceError.INVALID_OR_MISSING_DATA);
		}
	}

	/**
	 * Validate employee data.
	 *
	 * @param empcode
	 *            the empcode
	 * @param identity
	 *            the identity
	 * @param ipAddress
	 *            the ip address
	 * @return the employee
	 */
	public Employee validateEmployeeData(String empcode, String identity, String ipAddress) {
		Employee emp = loginDao.validateEmpDetails(empcode, identity, ipAddress);
		return emp;
	}

}
