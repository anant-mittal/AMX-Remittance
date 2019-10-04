/**
 * 
 */
package com.amx.jax.rbaac;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dto.DeviceDto;
import com.amx.jax.rbaac.dto.DevicePairOtpResponse;
import com.amx.jax.rbaac.dto.request.DeviceRegistrationRequest;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.request.NotpDTO;
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
import com.amx.jax.rest.RestService;

/**
 * The Class RbaacServiceClient.
 *
 * @author abhijeet
 */
@Component
public class RbaacServiceClient implements IRbaacService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceClient.class);

	/** The rest service. */
	@Autowired
	RestService restService;

	/** The app config. */
	@Autowired
	AppConfig appConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#initAuthForUser(com.amx.jax.rbaac.dto.
	 * request. UserAuthInitReqDTO)
	 */
	@Override
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO) {

		String ipAddr = "";

		if (null != userAuthInitReqDTO.getUserClientDto()) {
			ipAddr = userAuthInitReqDTO.getUserClientDto().getGlobalIpAddress();
		}

		LOGGER.info("Init Auth Request called for Employee No: {}, Identity: {}, from IP address: {}, with TraceId: {}",
				userAuthInitReqDTO.getEmployeeNo(), userAuthInitReqDTO.getIdentity(), ipAddr,
				AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.INIT_AUTH).post(userAuthInitReqDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<UserAuthInitResponseDTO, Object>>() {
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDTO) {

		LOGGER.info("Authorisation Request called for Employee No: {}, from IP address: {}, with TraceId: {}",
				reqDTO.getEmployeeNo(), reqDTO.getIpAddress(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.AUTHORIZE).post(reqDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<EmployeeDetailsDTO, Object>>() {
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getAllPermissions(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(String ipAddress, String deviceId) {

		LOGGER.info("Received request for User Permissions, from IP address: {}, device Id: {}, with TraceId: {}",
				ipAddress, deviceId, AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.PERMS_GET).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<PermissionResposeDTO, Object>>() {
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getAllRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(String ipAddress, String deviceId) {

		LOGGER.info("Received request for User Roles, from IP address: {}, device Id: {}, with TraceId: {}", ipAddress,
				deviceId, AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLES_GET).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoleResponseDTO, Object>>() {
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#saveRole(com.amx.jax.rbaac.dto.request.
	 * RoleRequestDTO)
	 * 
	 */
	@Override
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(RoleRequestDTO roleRequestDTO) {

		LOGGER.info("Received request for Save Role, from IP address: {}, device Id: {}, with TraceId: {}",
				roleRequestDTO.getIpAddr(), roleRequestDTO.getDeviceId(), AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLES_SAVE).post(roleRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<RoleResponseDTO, Object>>() {
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getUserRoleMappingsForBranch(java.math.
	 * BigDecimal, java.lang.String, java.lang.String)
	 */
	@Override
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(BigDecimal countryBranchId,
			String ipAddress, String deviceId) {

		LOGGER.info(
				"Received request for Get User Role Allocations For Branch Id: {} , from IP address: {}, device Id: {}, with TraceId: {}",
				countryBranchId, ipAddress, deviceId, AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.RA_GET_FOR_BRANCH)
				.queryParam("countryBranchId", countryBranchId).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<UserRoleMappingsResponseDTO, Object>>() {
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
	 * request.UserRoleMappingsRequestDTO)
	 */
	@Override
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(UserRoleMappingsRequestDTO urmRequestDTO) {

		LOGGER.info("Received request for Update User Role Allocations " + " from Ip Address: "
				+ urmRequestDTO.getIpAddr() + " from device Id: " + urmRequestDTO.getDeviceId() + ", with TraceId: "
				+ AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.RA_UPDATE).post(urmRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<UserRoleMappingDTO, Object>>() {
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#updateEmployeeAccountDetails(com.amx.jax.
	 * rbaac .dto.request.EmployeeDetailsRequestDTO)
	 */
	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			EmployeeDetailsRequestDTO edRequestDTO) {

		LOGGER.info("Received request for Update User Account Details " + " from Ip Address: "
				+ edRequestDTO.getIpAddr() + " from device Id: " + edRequestDTO.getDeviceId() + " with TraceId: "
				+ AppContextUtil.getTraceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.UAC_UPDATE).post(edRequestDTO)
				.as(new ParameterizedTypeReference<AmxApiResponse<EmployeeDetailsDTO, Object>>() {
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#testGet()
	 */
	@Override
	public AmxApiResponse<String, Object> testGet() {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.TEST_GET).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<String, Object>>() {
				});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#testPost()
	 */
	@Override
	public AmxApiResponse<String, Object> testPost() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<NotpDTO, Object> verifyOTP(NotpDTO reqDTO) {
		LOGGER.debug("verify OTP");
		String url = appConfig.getAuthURL() + ApiEndPoints.NOTP_VERIFY;
		return restService.ajax(url).post(reqDTO).as(new ParameterizedTypeReference<AmxApiResponse<NotpDTO, Object>>() {
		});

	}

	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(DeviceRegistrationRequest request) {
		LOGGER.debug("in registerNewDevice");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_REG;
		return restService.ajax(url).post(request)
				.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(Integer deviceRegId, String mOtp) {
		LOGGER.debug("in activateDevice");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_ACTIVATE;
		return restService.ajax(url).field(Params.MOTP, mOtp).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(Integer deviceRegId) {
		LOGGER.debug("in deactivateDevice");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_DEACTIVATE;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(Integer deviceRegId, String paireToken) {
		LOGGER.debug("in createDeviceSession");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_CREATE_SESSION;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).field(Params.PAIRE_TOKEN, paireToken)
				.postForm().as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(ClientType deviceType,
			Integer countryBranchSystemInventoryId, String otp) {
		LOGGER.debug("in pairDeviceSession");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_PAIR_SESSION;
		return restService.ajax(url).field(Params.DEVICE_TYPE, deviceType)
				.field(Params.TERMINAL_ID, countryBranchSystemInventoryId).field(Params.OTP, otp).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, BoolRespModel>>() {
				});
	}

	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceSessionToken(BigDecimal deviceRegId,
			String deviceSessionToken) {
		LOGGER.debug("in validateDeviceSessionToken");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_VALIDATE_SESSION_TOKEN;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId)
				.field(Params.SESSION_TOKEN, deviceSessionToken).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<DevicePairOtpResponse, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(ClientType deviceClientType,
			BigDecimal countryBranchSystemInventoryId) {
		LOGGER.debug("in getDeviceRegIdByBranchInventoryId");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_GET_DEVICE_REG_ID;
		return restService.ajax(url).field(Params.DEVICE_CLIENT_TYPE, deviceClientType)
				.field(Params.DEVICE_SYS_INV_ID, countryBranchSystemInventoryId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BigDecimal, Object>>() {
				});
	}
	
	@Override
	public AmxApiResponse<DeviceDto, Object> getDeviceByDeviceRegId(BigDecimal deviceRegId) {
		LOGGER.debug("in getDeviceByDeviceRegId");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_GET_DEVICE_BY_DEVICE_REG_ID;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<RoleMappingForEmployee, Object> getRoleMappingsForEmployee(BigDecimal employeeId,
			String ipAddress, String deviceId, Boolean filterRole) {

		LOGGER.debug("in getRoleMappingsForEmployee");

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.GET_ROLE_MAPPING_FOR_EMPLOYEE)
				.queryParam("employeeId", employeeId).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).queryParam("filterRole", filterRole).post()
				.as(new ParameterizedTypeReference<AmxApiResponse<RoleMappingForEmployee, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> createEmployeeSystemMapping(BigDecimal employeeId,
			BigDecimal countryBranchSystemInventoryId) {

		LOGGER.debug("in createEmployeeSystemMapping");

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.EMPLOYEE_SYSTEM_MAPPING_CREATE)
				.field(Params.EMPLOYEE_ID, employeeId).field(Params.TERMINAL_ID, countryBranchSystemInventoryId)
				.postForm().as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> deleteDevice(Integer deviceRegId) {
		LOGGER.debug("in deleteDevice");
		String url = appConfig.getAuthURL() + ApiEndPoints.DEVICE_DELETE;
		return restService.ajax(url).field(Params.DEVICE_REG_ID, deviceRegId).postForm()
				.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
				});
	}

	@Override
	public AmxApiResponse<DeviceDto, Object> getDevicesByTerminal(BigDecimal terminalId, String terminalIp) {
		LOGGER.debug("get devices by terminal");
		String url = appConfig.getAuthURL() + ApiEndPoints.FIND_DEVICES_BY_TERMINAL;
		return restService.ajax(url).queryParam(Params.TERMINAL_ID, terminalId)
				.queryParam(Params.TERMINAL_IP, terminalIp).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
		});
	}

	@Override
	public AmxApiResponse<DeviceDto, Object> getDevicesByRegId(BigDecimal deviceRegId, String deviceId) {
		LOGGER.debug("get devices by device");
		String url = appConfig.getAuthURL() + ApiEndPoints.FIND_DEVICES_BY_ID;
		return restService.ajax(url).queryParam(Params.DEVICE_REG_ID, deviceRegId)
				.queryParam(Params.DEVICE_CLIENT_ID, deviceId).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<DeviceDto, Object>>() {
		});
	}
}
