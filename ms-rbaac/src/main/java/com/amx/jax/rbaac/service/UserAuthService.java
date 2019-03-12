/**
 * 
 */
package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.AmxConstants;
import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.dbmodel.Device;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.constants.RbaacServiceConstants;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dbmodel.ViewExEmpBranchSysDetails;
import com.amx.jax.rbaac.dto.UserClientDto;
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
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil.HashBuilder;
import com.amx.utils.Random;

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

	@Autowired
	DeviceService deviceService;

	@Autowired
	private AppConfig appConfig;

	/**
	 * Verify user details.
	 * 
	 * @param employeeNo the emp code
	 * @param identity   the identity
	 * @param ipAddress  the ip address
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

		UserClientDto userClientDto = userAuthInitReqDTO.getUserClientDto();

		String ipAddress = userClientDto.getGlobalIpAddress();
		// String deviceId = userClientDto.getDeviceId();
		DeviceType deviceType = userClientDto.getDeviceType();

		String partnerIdentity = userAuthInitReqDTO.getPartnerIdentity();
		LOGIN_TYPE loginType = userAuthInitReqDTO.getLoginType();

		// Login Type Assisted Check
		boolean isAssisted = null == loginType ? false : (LOGIN_TYPE.ASSISTED.equals(loginType) ? true : false);

		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(employeeNo) || StringUtils.isBlank(identity) || StringUtils.isBlank(ipAddress)
				|| deviceType == null || StringUtils.isBlank(deviceType.toString())) {
			throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_CREDENTIALS,
					"Employee Number, Civil Id, IP Address, & Device Type are Manadatory");
		}

		/**
		 * Check if login type is Assisted
		 */
		if (isAssisted) {
			if (StringUtils.isBlank(partnerIdentity)) {
				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_PARTNER_IDENTITY,
						"Partner Identity is Manadatory for Assisted Login");
			} else if (partnerIdentity.equalsIgnoreCase(identity)) {
				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_PARTNER_IDENTITY,
						"Partner Identity can not be same as Primary User Identity");
			}
		}

		List<Employee> employees = rbaacDao.getEmployees(employeeNo, identity);

		Employee selfEmployee = getValidEmployee(employees, "Self");

		// Validate Employee Device/Terminal Assignment

		validateLoginClient(selfEmployee, userAuthInitReqDTO);

		/**
		 * Begin Init Auth for User validation is Completed.
		 */

		String selfOtpSecret = AmxConstants.SHH_DONT_TELL_ANYONE + Random.randomAlphaNumeric(AmxConstants.OTP_LENGTH);

		OtpData selfOtpData = userOtpManager.generateOtpTokens(selfOtpSecret, userAuthInitReqDTO.getSelfSAC());

		userOtpManager.sendOtpSms(selfEmployee, selfOtpData, "Self OTP Details");

		String transactionId = AppContextUtil.getTranxId();

		UserOtpData userOtpData = new UserOtpData();

		userOtpData.setEmployee(selfEmployee);

		userOtpData.setLoginType(userAuthInitReqDTO.getLoginType());
		userOtpData.setOtpData(selfOtpData);
		userOtpData.setAuthTransactionId(transactionId);

		// Set OTP Attempt
		userOtpData.setOtpAttemptCount(0);

		// Set Init Time and Time to live

		/**
		 * For Assisted Login;
		 */
		Employee partnerEmployee = new Employee();

		if (isAssisted) {
			List<Employee> possiblePartners = rbaacDao.getEmployeesByCivilId(partnerIdentity);

			partnerEmployee = getValidEmployee(possiblePartners, "Partner");

			userOtpData.setPartnerEmployeeId(partnerEmployee.getEmployeeId());

			// Device partnerOTPDevice =
			// deviceService.getDeviceByEmployeeAndDeviceType(ClientType.NOTP_APP,
			// selfEmployee.getEmployeeId());

			String partnerOTPSecret = AmxConstants.SHH_DONT_TELL_ANYONE
					+ Random.randomAlphaNumeric(AmxConstants.OTP_LENGTH);

			/*
			 * if (!ArgUtil.isEmpty(partnerOTPDevice)) { partnerOTPDeviceSecret =
			 * partnerOTPDevice.getClientSecreteKey(); }
			 */
			OtpData partnerOtpData = userOtpManager.generateOtpTokens(partnerOTPSecret,
					userAuthInitReqDTO.getPartnerSAC());
			userOtpManager.sendOtpSms(partnerEmployee, partnerOtpData, "Partner OTP Details");
			userOtpData.setPartnerOtpData(partnerOtpData);
		}

		userOtpCache.cacheUserOtpData(selfEmployee.getEmployeeNumber(), userOtpData);

		UserAuthInitResponseDTO dto = new UserAuthInitResponseDTO();
		dto.setEmployeeId(selfEmployee.getEmployeeId());

		dto.setAuthTransactionId(transactionId);

		// Set Self Otp Prefix
		dto.setmOtpPrefix(selfOtpData.getmOtpPrefix());

		// partner Otp Prefix
		if (isAssisted) {
			dto.setPartnerMOtpPrefix(userOtpData.getPartnerOtpData().getmOtpPrefix());
			dto.setPartnerEmployeeId(partnerEmployee.getEmployeeId());
		}

		dto.setInitOtpTime(String.valueOf(selfOtpData.getInitTime()));
		dto.setTtlOtp(String.valueOf(selfOtpData.getTtl()));

		/**
		 * Send Offline OTP to Slack - only for TEST
		 */
		if (!appConfig.isProdMode()) {

			Device otpDevice = deviceService.getDeviceByEmployeeAndDeviceType(ClientType.NOTP_APP,
					selfEmployee.getEmployeeId());

			if (!ArgUtil.isEmpty(otpDevice)) {

				HashBuilder builder = new HashBuilder().currentTime(System.currentTimeMillis())
						.interval(AmxConstants.OFFLINE_OTP_TTL).tolerance(AmxConstants.OFFLINE_OTP_TOLERANCE)
						.secret(otpDevice.getClientSecreteKey()).message(selfOtpData.getmOtpPrefix());

				userOtpManager.sendToSlack("Offline OTP for Emp: " + employeeNo, " Self ", selfOtpData.getmOtpPrefix(),
						builder.toHMAC().toNumeric(AmxConstants.OTP_LENGTH).output());

			}

			if (isAssisted) {

				Device partnerOtpDevice = deviceService.getDeviceByEmployeeAndDeviceType(ClientType.NOTP_APP,
						partnerEmployee.getEmployeeId());

				if (!ArgUtil.isEmpty(partnerOtpDevice)) {

					HashBuilder builderP = new HashBuilder().currentTime(System.currentTimeMillis())
							.interval(AmxConstants.OFFLINE_OTP_TTL).tolerance(AmxConstants.OFFLINE_OTP_TOLERANCE)
							.secret(partnerOtpDevice.getClientSecreteKey())
							.message(userOtpData.getPartnerOtpData().getmOtpPrefix());

					userOtpManager.sendToSlack("Offline OTP for Emp: " + employeeNo, " Partner ",
							userOtpData.getPartnerOtpData().getmOtpPrefix(),
							builderP.toHMAC().toNumeric(AmxConstants.OTP_LENGTH).output());

				}

			}

		}

		LOGGER.info("OTP(s) generated for Employee No: " + selfEmployee.getEmployeeNumber());

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
		String ipAddress = reqDto.getIpAddress();
		// String transactionId = reqDto.getTransactionId();
		String deviceId = reqDto.getDeviceId();

		/**
		 * Input -> Invalid
		 */
		if (StringUtils.isBlank(employeeNo) || StringUtils.isBlank(mOtp) || StringUtils.isBlank(ipAddress)) {
			throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_DATA,
					"Employee Number, Otp & IP Address are Manadatory");
		}

		// Get Cached OTP data for the user.
		UserOtpData userOtpData = userOtpCache.get(employeeNo);

		// NO OTP data for user.
		// handle Time out Here.
		// eOtp is not checked
		if (userOtpData == null || StringUtils.isBlank(userOtpData.getOtpData().getHashedmOtp())) {
			throw new AuthServiceException(RbaacServiceError.INVALID_OTP,
					"Invalid OTP: OTP is not generated for the user or timedOut");
		}

		/**
		 * Check for Timed Out OTP
		 */
		if (userOtpData.getOtpData().getInitTime() + AmxConstants.SMS_OTP_TTL * 1000 < System.currentTimeMillis()) {

			userOtpCache.remove(employeeNo);

			throw new AuthServiceException(RbaacServiceError.OTP_TIMED_OUT, "Invalid OTP: OTP is timedOut");
		}

		Employee employee = userOtpData.getEmployee();

		String mOtpHash = UserOtpManager.getOtpHash(mOtp);
		String partnerOtpHash = "";
		boolean isAssisted = LOGIN_TYPE.ASSISTED.equals(userOtpData.getLoginType()) ? true : false;

		if (isAssisted) {

			if (StringUtils.isBlank(reqDto.getPartnerMOtp())) {
				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_DATA,
						"Partner Otp is Manadatory for Assisted Login");
			}

			partnerOtpHash = UserOtpManager.getOtpHash(reqDto.getPartnerMOtp());

			if (StringUtils.isBlank(userOtpData.getPartnerOtpData().getHashedmOtp())) {
				throw new AuthServiceException(RbaacServiceError.INVALID_PARTNER_OTP,
						"Invalid OTP: OTP is not generated for the Partner user or timedOut");
			}

		}

		/**
		 * ========== Start : Code for Deciding Authorization ==========
		 */

		boolean isAuthorized = Boolean.FALSE;

		if (userOtpData.getOtpData().getHashedmOtp().equals(mOtpHash)) {
			/**
			 * SMS Otp is validated
			 */
			isAuthorized = Boolean.TRUE;
		} else {
			/**
			 * Check if Offline OTP is valid for Self
			 */
			isAuthorized = validateOfflineOtp(mOtp, employee.getEmployeeId(), userOtpData.getOtpData().getmOtpPrefix());
		}

		/**
		 * Case where self OTP is validated and Login Type is Assisted.
		 */
		if (isAuthorized && isAssisted) {
			if (userOtpData.getPartnerOtpData().getHashedmOtp().equals(partnerOtpHash)) {
				/**
				 * Partner SMS OTP is Valid
				 */
				isAuthorized = Boolean.TRUE;
			} else {
				/**
				 * Validate Partner Offline OTP
				 */
				isAuthorized = validateOfflineOtp(reqDto.getPartnerMOtp(), userOtpData.getPartnerEmployeeId(),
						userOtpData.getPartnerOtpData().getmOtpPrefix());
			}
		}

		/**
		 * ========== End : Code for Deciding Authorization ==========
		 */

		if (!isAuthorized) {

			/**
			 * Crossed three Incorrect OTP counts --> Lock Accounnt. --> Clear Otp Cache
			 */
			if (userOtpData.getOtpAttemptCount() >= 2) {

				// Implement Lock user Account
				this.lockUserAccount(employee);

				// Clear OTP Cache
				userOtpCache.remove(employeeNo);

				throw new AuthServiceException(RbaacServiceError.USER_ACCOUNT_LOCKED,
						"Invalid OTP : Max OTP Attempts are Exeeded : User Account is LOCKED ");
			}

			// Normal Incorrect Attempt: Increment Count
			userOtpData.incrementOtpAttemptCount();

			userOtpCache.cacheUserOtpData(employeeNo, userOtpData);

			throw new AuthServiceException(RbaacServiceError.INVALID_OTP, "Invalid OTP: OTP entered is Incorrect");
		}

		// OTP is validated
		userOtpCache.remove(employeeNo);

		EmployeeDetailsDTO empDetail = ObjectConverter.convertEmployeeToEmpDetailsDTO(employee);

		RoleResponseDTO roleResponseDTO = getRoleForUser(employee.getEmployeeId());

		empDetail.setUserRole(roleResponseDTO);

		// Set Last Successful Login Date as Current Date
		updateLastLogin(employee);

		LOGGER.info("Login Access granted for Employee No: " + employee.getEmployeeNumber() + " from IP : " + ipAddress
				+ " from Device id : " + deviceId);

		return empDetail;
	}

	/**
	 * 
	 * @param otp
	 * @param employeeId
	 * @param initOtpData
	 * @return
	 */
	public boolean validateOfflineOtp(String otp, BigDecimal employeeId, String sac) {

		Device otpDevice = deviceService.getDeviceByEmployeeAndDeviceType(ClientType.NOTP_APP, employeeId);

		if (ArgUtil.isEmpty(otpDevice) || !RbaacServiceConstants.YES.equalsIgnoreCase(otpDevice.getStatus())) {
			return Boolean.FALSE;
		}

		HashBuilder builder = new HashBuilder().currentTime(System.currentTimeMillis())
				.interval(AmxConstants.OFFLINE_OTP_TTL).tolerance(AmxConstants.OFFLINE_OTP_TOLERANCE)
				.secret(otpDevice.getClientSecreteKey()).message(sac);

		if (!builder.validateNumHMAC(otp)) {
			return Boolean.FALSE;
		}

		return Boolean.TRUE;

	}

	private Employee getValidEmployee(List<Employee> employees, String userType) {

		// LOGIN_TYPE userType = userAuthInitReqDTO.getLoginType();

		/**
		 * Invalid Employee Details
		 */
		if (null == employees || employees.isEmpty()) {
			throw new AuthServiceException(RbaacServiceError.INVALID_USER_DETAILS,
					"Employee Details not available : " + userType);
		}

		List<Employee> activeEmployees = new ArrayList<Employee>();
		/**
		 * Filter Out InActive Employees
		 */
		employees.forEach(employee -> {
			if (StringUtils.isNotBlank(employee.getIsActive()) && "Y".equalsIgnoreCase(employee.getIsActive())) {
				activeEmployees.add(employee);
			}
		});

		if (activeEmployees.isEmpty()) {
			throw new AuthServiceException(RbaacServiceError.USER_NOT_ACTIVE_OR_DELETED,
					"User Not Active Or Deleted: User Account is Suspended : " + userType);
		}

		/**
		 * Check for Multiple Employees
		 */
		if (activeEmployees.size() > 1) {
			throw new AuthServiceException(RbaacServiceError.MULTIPLE_USERS,
					"Multiple Users Corresponding to the same Info: Pls contact Support : " + userType);
		}

		Employee validEmployee = activeEmployees.get(0);

		/**
		 * Check if user A/C is Locked. lockcnt >= 3
		 */
		if (null != validEmployee.getLockCount()
				&& validEmployee.getLockCount().intValue() >= RbaacServiceConstants.EMPLOYEE_MAX_LOCK_COUNT) {
			throw new AuthServiceException(RbaacServiceError.USER_ACCOUNT_LOCKED,
					"User Account Locked : User Account Login is Suspended, from: " + validEmployee.getLockDate()
							+ " for Login Type : " + userType);
		}

		/**
		 * Check if Employee has a Phone Number.
		 * 
		 * Validity of phone number is not checked.
		 */

		if (StringUtils.isEmpty(validEmployee.getTelephoneNumber())) {
			throw new AuthServiceException(RbaacServiceError.INVALID_PHONE_NUMBER,
					"The mobile number is invalid for the user");
		}

		return validEmployee;
	}

	private boolean validateLoginClient(Employee employee, UserAuthInitReqDTO userAuthInitReqDTO) {

		UserClientDto userClientDto = userAuthInitReqDTO.getUserClientDto();

		if (null == userClientDto) {
			throw new AuthServiceException(RbaacServiceError.CLIENT_NOT_FOUND, "User Client Info Is Null");
		}

		DeviceType deviceType = userClientDto.getDeviceType();

		// Check for Employee System Assignment
		if (DeviceType.COMPUTER.isParentOf(deviceType)) {

			if (null == userClientDto.getTerminalId()) {

				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_TERMINAL_ID,
						"Terminal Id is Mandatory for Computer Terminals");
			}

			List<ViewExEmpBranchSysDetails> empBranchSysDetails = rbaacDao
					.getEmpBranchSysDetailsByEmpIdAndBranchSysInventoryId(employee.getEmployeeId(),
							userAuthInitReqDTO.getUserClientDto().getTerminalId());

			if (null == empBranchSysDetails || empBranchSysDetails.isEmpty()) {

				throw new AuthServiceException(RbaacServiceError.BRANCH_SYSTEM_NOT_FOUND,
						"Branch System/Terminal is Invalid, NONE found to be assigned.");

			}
			if (empBranchSysDetails.size() > 1) {

				LOGGER.warn("Multiple Active Terminals with Same Terminal Id: "
						+ userAuthInitReqDTO.getUserClientDto().getTerminalId() + " exist for Employee Id : "
						+ employee.getEmployeeId());

			}

			return Boolean.TRUE;

		} else if (DeviceType.MOBILE.isParentOf(deviceType)) {

			// Device and terminal Validations
			if (StringUtils.isBlank(userClientDto.getDeviceId()) || null == userClientDto.getDeviceRegId()
					|| StringUtils.isBlank(userClientDto.getDeviceRegToken())) {

				throw new AuthServiceException(RbaacServiceError.INVALID_OR_MISSING_DEVICE_ID,
						"Valid Device Info is Mandatory for Mobile/Tablet Devices, DeviceId: "
								+ userClientDto.getDeviceId() + ", Device Reg Id: " + userClientDto.getDeviceRegId()
								+ " Device Reg Token: " + userClientDto.getDeviceRegToken());

			}

			return deviceService.validateEmployeeDeviceMapping(employee.getEmployeeId(), userClientDto.getDeviceId(),
					userClientDto.getDeviceRegId(), userClientDto.getDeviceRegToken());

		}

		return Boolean.FALSE;
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

	private boolean updateLastLogin(Employee srcEmp) {

		Employee destEmp = rbaacDao.getEmployeeByEmployeeId(srcEmp.getEmployeeId());
		destEmp.setLastLogin(new Date());

		rbaacDao.saveEmployee(destEmp);

		return true;
	}

}
