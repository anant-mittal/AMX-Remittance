/**
 * 
 */
package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AppContextUtil;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.RbaacConstants;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.DEVICE_TYPE;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.jax.rbaac.manager.UserOtpManager;
import com.amx.jax.rbaac.trnx.UserOtpCache;
import com.amx.jax.rbaac.trnx.UserOtpData;
import com.amx.jax.util.ObjectConverter;

/**
 * The Class UserAuthService.
 *
 * @author abhijeet
 */
@Service
public class UserAuthService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(UserAuthService.class);

	/** The login dao. */
	@Autowired
	RbaacDao rbaacDao;

	/** The user otp data. */
	@Autowired
	UserOtpCache userOtpCache;

	/** The user otp manager. */
	@Autowired
	UserOtpManager userOtpManager;

	/**
	 * Verify user details.
	 * 
	 * @param employeeNo
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
	public UserAuthInitResponseDTO verifyUserDetails(UserAuthInitReqDTO userAuthInitReqDTO) {

		String employeeNo = userAuthInitReqDTO.getEmployeeNo();
		String identity = userAuthInitReqDTO.getIdentity();
		String ipAddress = userAuthInitReqDTO.getIpAddress();
		String deviceId = userAuthInitReqDTO.getDeviceId();
		DEVICE_TYPE deviceType = userAuthInitReqDTO.getDeviceType();

		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(employeeNo) || StringUtils.isBlank(identity) || StringUtils.isBlank(ipAddress)
				|| deviceType == null || StringUtils.isBlank(deviceType.toString())) {
			throw new AuthServiceException("Employee Number, Civil Id, IP Address, & Device Type are Manadatory",
					RbaacServiceError.INVALID_OR_MISSING_DATA);
		}

		if (DEVICE_TYPE.MOBILE.equals(deviceType) && StringUtils.isBlank(deviceId)) {
			throw new AuthServiceException("Device Id is Mandatory for Mobile Devices",
					RbaacServiceError.INVALID_OR_MISSING_DATA);
		}

		List<Employee> employees;

		if (DEVICE_TYPE.MOBILE.equals(deviceType)) {
			employees = rbaacDao.getEmployeesByDeviceId(employeeNo, identity, deviceId);
		} else {
			employees = rbaacDao.getEmployees(employeeNo, identity, ipAddress);
		}

		/**
		 * Invalid Employee Details
		 */
		if (null == employees || employees.isEmpty()) {
			throw new AuthServiceException("Employee Details not available", RbaacServiceError.INVALID_USER_DETAILS);
		}

		/**
		 * Check for Multiple Employees
		 */
		if (employees.size() > 1) {
			throw new AuthServiceException("Multiple Users Corresponding to the same Info: Pls contact Support",
					RbaacServiceError.MULTIPLE_USERS);
		}

		Employee emp = employees.get(0);

		/**
		 * Check if User is Active
		 */
		if (StringUtils.isBlank(emp.getIsActive()) || !"Y".equalsIgnoreCase(emp.getIsActive())
				|| "D".equalsIgnoreCase(emp.getDeletedUser()) || "Y".equalsIgnoreCase(emp.getDeletedUser())) {
			throw new AuthServiceException("User Not Active Or Deleted: User Account is Suspended.",
					RbaacServiceError.USER_NOT_ACTIVE_OR_DELETED);
		}

		/**
		 * Check if user A/C is Locked. lockcnt >= 3
		 */
		if (null != emp.getLockCount() && emp.getLockCount().intValue() >= RbaacConstants.EMPLOYEE_MAX_LOCK_COUNT) {
			throw new AuthServiceException(
					"User Account Locked : User Account Login is Suspended, from: " + emp.getLockDate(),
					RbaacServiceError.USER_ACCOUNT_LOCKED);
		}

		/**
		 * Begin Init Auth for All User validation is Completed.
		 */
		OtpData otpData = userOtpManager.generateOtpTokens();

		userOtpManager.sendOtpSms(emp, otpData);

		String transactionId = AppContextUtil.getTranxId();

		UserOtpData userOtpData = new UserOtpData();

		userOtpData.setEmployee(emp);
		userOtpData.setOtpData(otpData);
		userOtpData.setAuthTransactionId(transactionId);

		// Set OTP Attempt
		userOtpData.setOtpAttemptCount(0);

		userOtpCache.fastPut(emp.getEmployeeNumber(), userOtpData);

		UserAuthInitResponseDTO dto = new UserAuthInitResponseDTO();

		dto.setAuthTransactionId(transactionId);
		dto.setmOtpPrefix(otpData.getmOtpPrefix());
		dto.setInitOtpTime(String.valueOf(otpData.getInitTime()));
		dto.setTtlOtp(String.valueOf(otpData.getTtl()));

		LOGGER.info("OTP generated for Employee No: " + emp.getEmployeeNumber());

		return dto;

	}

	/**
	 * 
	 * @param employeeNo
	 * @param mOtpHash
	 * @param eOtpHash
	 * @param ipAddress
	 * @return Employees Details with Roles
	 * 
	 * @Flow : Validate if init auth session exists || Check for OTP attempts ||
	 *       Check if OTP is valid || grant access || clear Cache.
	 * 
	 */
	public EmployeeDetailsDTO authoriseUser(UserAuthorisationReqDTO reqDto) {

		String employeeNo = reqDto.getEmployeeNo();
		String mOtp = reqDto.getmOtp();
		// String eOtpHash = reqDto.geteOtpHash();
		String ipAddress = reqDto.getIpAddress();
		// String transactionId = reqDto.getTransactionId();
		String deviceId = reqDto.getDeviceId();

		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(employeeNo) || StringUtils.isBlank(mOtp) || StringUtils.isBlank(ipAddress)) {
			throw new AuthServiceException("Employee Number, Otp & IP Address are Manadatory",
					RbaacServiceError.INVALID_OR_MISSING_DATA);
		}

		// Get Cached OTP data for the user.
		UserOtpData userOtpData = userOtpCache.get(employeeNo);

		// NO OTP data for user.
		// handle Time out Here.
		// eOtp is not checked
		if (userOtpData == null || StringUtils.isBlank(userOtpData.getOtpData().getHashedmOtp())) {
			throw new AuthServiceException("Invalid OTP: OTP is not generated for the user or timedOut",
					RbaacServiceError.INVALID_OTP);
		}

		Employee employee = userOtpData.getEmployee();

		String mOtpHash = UserOtpManager.getOtpHash(mOtp);

		// Validate User OTP hash
		if (!userOtpData.getOtpData().getHashedmOtp().equals(mOtpHash)) {

			/**
			 * Crossed three Incorrect OTP counts --> Lock Accounnt. --> Clear Otp Cache
			 */
			if (userOtpData.getOtpAttemptCount() >= 2) {

				// Implement Lock user Account
				this.lockUserAccount(employee);

				// Clear OTP Cache
				userOtpCache.remove(employeeNo);

				throw new AuthServiceException("Invalid OTP : Max OTP Attempts are Exeeded : User Account is LOCKED ",
						RbaacServiceError.USER_ACCOUNT_LOCKED);
			}

			// Normal Incorrect Attempt: Increment Count
			userOtpData.incrementOtpAttemptCount();

			userOtpCache.fastPut(employeeNo, userOtpData);

			throw new AuthServiceException("Invalid OTP: OTP entered is Incorrect", RbaacServiceError.INVALID_OTP);
		}

		// OTP is validated
		userOtpCache.remove(employeeNo);

		EmployeeDetailsDTO empDetail = ObjectConverter.convertEmployeeToEmpDetailsDTO(employee);

		RoleResponseDTO roleResponseDTO = getRoleForUser(employee.getEmployeeId());

		empDetail.setUserRole(roleResponseDTO);

		LOGGER.info("Login Access granted for Employee No: " + employee.getEmployeeNumber() + " from IP : " + ipAddress
				+ " from Device id : " + deviceId);

		return empDetail;
	}

	private RoleResponseDTO getRoleForUser(BigDecimal userId) {

		UserRoleMapping urm = rbaacDao.getUserRoleMappingByEmployeeId(userId);

		if (urm == null) {
			return new RoleResponseDTO();
		}

		Role role = rbaacDao.getRoleById(urm.getRoleId());

		if (urm.getSuspended() != null
				&& (urm.getSuspended().equalsIgnoreCase("Y") || urm.getSuspended().equalsIgnoreCase("YES"))) {
			role.setSuspended("Y");
		}

		return ObjectConverter.convertRoleToRoleResponseDTO(role);
	}

	/**
	 * Lock user Account
	 */
	private boolean lockUserAccount(Employee srcEmp) {

		Employee destEmp = rbaacDao.getEmployeeByEmployeeId(srcEmp.getEmployeeId());
		destEmp.setLockCount(new BigDecimal(3));
		destEmp.setLockDate(new Date());

		rbaacDao.saveEmployee(destEmp);

		return true;
	}

}
