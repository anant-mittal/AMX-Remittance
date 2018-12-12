package com.amx.jax.rbaac;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.request.UserRoleMappingsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleMappingForEmployee;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingsResponseDTO;
import com.amx.jax.rbaac.error.RbaacApiStatusBuilder.RbaacApiStatus;
import com.amx.jax.rbaac.error.RbaacServiceError;

/**
 * The Interface IRbaacService.
 *
 * @author abhijeet
 */
public interface IRbaacService {

	/**
	 * The Class ApiEndPoints.
	 */
	public static class ApiEndPoints {

		/** The Constant SERVICE_PREFIX. */
		private static final String SERVICE_PREFIX = "/rbaac";

		/** The Constant API_VERSION_V1. */
		private static final String API_VERSION_V1 = "/v1";

		/** The Constant INIT_AUTH. */
		public static final String INIT_AUTH = SERVICE_PREFIX + API_VERSION_V1 + "/auth/init-auth";

		/** The Constant AUTHORIZE. */
		public static final String AUTHORIZE = SERVICE_PREFIX + API_VERSION_V1 + "/auth/authorize";

		/** The Constant PERMS_GET. */
		public static final String PERMS_GET = SERVICE_PREFIX + API_VERSION_V1 + "/perms/get";

		/** The Constant ROLES_GET. */
		public static final String ROLES_GET = SERVICE_PREFIX + API_VERSION_V1 + "/roles/get";

		/** The Constant ROLES_SAVE. */
		public static final String ROLES_SAVE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/save";

		/** The Constant RA_GET_FOR_BRANCH. */
		public static final String RA_GET_FOR_BRANCH = SERVICE_PREFIX + API_VERSION_V1 + "/roles/alloc/get-for-branch";

		/** The Constant RA_UPDATE. */
		public static final String RA_UPDATE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/alloc/update";

		/** The Constant UAC_UPDATE. */
		public static final String UAC_UPDATE = SERVICE_PREFIX + API_VERSION_V1 + "/user/account/update";

		/** The Constant TEST_GET. */
		public static final String TEST_GET = SERVICE_PREFIX + API_VERSION_V1 + "/test/get";

		/** The Constant TEST_POST. */
		public static final String TEST_POST = SERVICE_PREFIX + API_VERSION_V1 + "/test/post";
		
		public static final String DEVICE_PREFIX = SERVICE_PREFIX + API_VERSION_V1 + "/device";
		
		public static final String DEVICE_ACTIVATE = DEVICE_PREFIX + "/activate";
		public static final String DEVICE_DEACTIVATE = DEVICE_PREFIX + "/deactivate";
		public static final String DEVICE_CREATE_SESSION = DEVICE_PREFIX + "/createsession";
		public static final String DEVICE_PAIR_SESSION =  DEVICE_PREFIX + "/pairsession";
		public static final String DEVICE_VALIDATE_SESSION_TOKEN =  DEVICE_PREFIX + "/validate-sessiontoken";
		public static final String DEVICE_REG = DEVICE_PREFIX + "/register";
		public static final String DEVICE_GET_DEVICE_REG_ID =  DEVICE_PREFIX + "/get-deviceregid";
		
		/** The Constant GET_ROLE_MAPPING_FOR_EMPLOYEE. */
		public static final String GET_ROLE_MAPPING_FOR_EMPLOYEE = SERVICE_PREFIX + API_VERSION_V1 + "/roles/alloc/get-role-map-for-employee";
		
		public static final String EMPLOYEE_SYSTEM_MAPPING_CREATE =  SERVICE_PREFIX + API_VERSION_V1 + "/employee-system/create";

	}
	
	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String DEVICE_TYPE = "deviceType";
		public static final String DEVICE_REG_ID = "deviceRegId";
		public static final String SESSION_TOKEN = "sessionToken";
		public static final String PAIRE_TOKEN = "paireToken";
		public static final String DEVICE_CLIENT_TYPE = "deviceClientType";
		public static final String DEVICE_SYS_INV_ID = "countryBranchSystemInventoryId";
		public static final String OTP = "otp";
		public static final String MOTP = "mOtp";

	}

	/**
	 * Begins process of user Authentication with employee details - empCode,
	 * CivilId and Access Terminal IP .
	 *
	 * @param userAuthInitReqDTO
	 *            the user auth init req DTO
	 * @return the amx api response
	 */
	@RbaacApiStatus({ RbaacServiceError.INVALID_OR_MISSING_DATA, RbaacServiceError.INVALID_USER_DETAILS,
			RbaacServiceError.MULTIPLE_USERS, RbaacServiceError.USER_NOT_ACTIVE_OR_DELETED,
			RbaacServiceError.USER_ACCOUNT_LOCKED, RbaacServiceError.INVALID_OR_MISSING_CREDENTIALS,
			RbaacServiceError.INVALID_OR_MISSING_PARTNER_IDENTITY, RbaacServiceError.CLIENT_NOT_FOUND,
			RbaacServiceError.INVALID_OR_MISSING_TERMINAL_ID, RbaacServiceError.BRANCH_SYSTEM_NOT_FOUND,
			RbaacServiceError.INVALID_OR_MISSING_DEVICE_ID, RbaacServiceError.DEVICE_CLIENT_INVALID })
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO);

	/**
	 * Authorise user.
	 *
	 * @param reqDto
	 *            the req dto
	 * @return the amx api response
	 */
	@RbaacApiStatus({ RbaacServiceError.INVALID_OR_MISSING_DATA, RbaacServiceError.INVALID_OTP,
			RbaacServiceError.USER_ACCOUNT_LOCKED, RbaacServiceError.INVALID_PARTNER_OTP })
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDto);

	/**
	 * Gets the all permissions.
	 *
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the all permissions
	 */
	@RbaacApiStatus({ RbaacServiceError.INCOMPATIBLE_DATA_TYPE })
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(String ipAddr, String deviceId);

	/**
	 * Gets the all roles.
	 *
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the all roles
	 */
	@RbaacApiStatus({})
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(String ipAddr, String deviceId);

	/**
	 * Save role.
	 *
	 * @param roleRequestDTO
	 *            the role request DTO
	 * @return the amx api response
	 */
	@RbaacApiStatus({ RbaacServiceError.INVALID_ROLE, RbaacServiceError.DUPLICATE_ROLE,
			RbaacServiceError.INVALID_ROLE_DEFINITION, RbaacServiceError.INVALID_ACCESS_TYPE_SCOPE,
			RbaacServiceError.INVALID_PERMISSION })
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(RoleRequestDTO roleRequestDTO);

	/**
	 * Gets the user role mappings for branch.
	 *
	 * @param countryBranchId
	 *            the country branch id
	 * @param ipAddr
	 *            the ip addr
	 * @param deviceId
	 *            the device id
	 * @return the user role mappings for branch
	 */
	@RbaacApiStatus({})
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(BigDecimal countryBranchId,
			String ipAddr, String deviceId);

	/**
	 * Update user role mappings.
	 *
	 * @param urmRequestDTO
	 *            the urm request DTO
	 * @return the amx api response
	 */
	@RbaacApiStatus({ RbaacServiceError.INVALID_USER_ROLE_MAPPINGS,
			RbaacServiceError.ILLEGAL_USER_ROLE_MAPPING_MODIFICATION })
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(UserRoleMappingsRequestDTO urmRequestDTO);

	/**
	 * Update employee account details.
	 *
	 * @param employeeDTO
	 *            the employee DTO
	 * @return the amx api response
	 */
	@RbaacApiStatus({})
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			EmployeeDetailsRequestDTO edRequestDTO);

	/**
	 * Test get.
	 *
	 * @return the amx api response
	 */
	public AmxApiResponse<String, Object> testGet();

	/**
	 * Test post.
	 *
	 * @return the amx api response
	 */
	public AmxApiResponse<String, Object> testPost();
	
	
	/**
	 * registers new device
	 * @param request
	 * @return
	 * 
	 */
	@RbaacApiStatus({ RbaacServiceError.CLIENT_ALREADY_REGISTERED })
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request);

	/**
	 * activates device
	 * @param deviceRegId mandatory
	 * @param mOtp optional
	 * @return
	 * 
	 */
	@RbaacApiStatus({ RbaacServiceError.CLIENT_NOT_FOUND })
	public  AmxApiResponse<BoolRespModel, Object> activateDevice(Integer deviceRegId, String mOtp);

	/**
	 * @param deviceRegId
	 * @return
	 * 
	 */
	@RbaacApiStatus(RbaacServiceError.CLIENT_NOT_FOUND)
	public  AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId);

	/**
	 * creates session
	 * @param deviceRegId
	 * @param paireToken
	 * @return
	 * 
	 */
	public  AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(Integer deviceRegId, String paireToken);

	/**
	 * pair device session
	 * @param deviceType
	 * @param countryBranchSystemInventoryId
	 * @param otp
	 * @return
	 * 
	 */
	public  AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp);

	/**
	 * validates device session token
	 * @param deviceRegId
	 * @param deviceSessionToken
	 * @return
	 * 
	 */
	public  AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceSessionToken(BigDecimal deviceRegId,
			String deviceSessionToken);

	/**
	 * 
	 * @param deviceClientType
	 * @param countryBranchSystemInventoryId
	 * @return device registration id
	 * 
	 */
	public  AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId);
	
	
	@RbaacApiStatus({})
	public AmxApiResponse<RoleMappingForEmployee, Object> getRoleMappingsForEmployee(BigDecimal employeeId,
			String ipAddr, String deviceId, Boolean filterRole);

	AmxApiResponse<BoolRespModel, Object> createEmployeeSystemMapping(BigDecimal employeeId,
			Integer countryBranchSystemInventoryId);

}
