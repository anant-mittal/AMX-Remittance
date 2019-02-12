/**
 * 
 */
package com.amx.jax.rbaac.api;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppContextUtil;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.rbaac.IRbaacService;
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
import com.amx.jax.rbaac.service.DeviceService;
import com.amx.jax.rbaac.service.RespTestService;
import com.amx.jax.rbaac.service.UserAccountService;
import com.amx.jax.rbaac.service.UserAuthService;
import com.amx.jax.rbaac.service.UserRoleService;

/**
 * The Class RbaacServiceApiController.
 *
 * @author abhijeet
 */
@RestController
public class RbaacServiceApiController implements IRbaacService {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RbaacServiceApiController.class);

	/** The user auth service. */
	@Autowired
	UserAuthService userAuthService;

	/** The resp test service. */
	@Autowired
	RespTestService respTestService;

	/** The user role service. */
	@Autowired
	UserRoleService userRoleService;

	/** The user account service. */
	@Autowired
	UserAccountService userAccountService;

	@Autowired
	HttpServletRequest request;

	@Autowired
	DeviceService deviceService;

	/**
	 * Init User Authentication.
	 *
	 * @param userAuthInitReqDTO the user auth init req DTO
	 * @return the amx api response
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(
			@RequestBody @Valid UserAuthInitReqDTO userAuthInitReqDTO) {

		LOGGER.info("Begin Init Auth for User: " + userAuthInitReqDTO.getEmployeeNo() + " from Ip Address: "
				+ userAuthInitReqDTO.getUserClientDto().getGlobalIpAddress() + " from device Id: "
				+ userAuthInitReqDTO.getUserClientDto().getDeviceId() + " with TraceId: "
				+ AppContextUtil.getTraceId());

		LOGGER.info("User Auth Init Request Dto : " + userAuthInitReqDTO.toString());

		UserAuthInitResponseDTO userAuthInitResponseDTO = userAuthService.verifyUserDetails(userAuthInitReqDTO);

		return AmxApiResponse.build(userAuthInitResponseDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.AUTHORIZE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(
			@RequestBody @Valid UserAuthorisationReqDTO reqDto) {

		LOGGER.info("Received request for authorising User Access : " + reqDto.getEmployeeNo() + " from Ip Address: "
				+ reqDto.getIpAddress() + " from device Id: " + reqDto.getDeviceId() + " TraceId: "
				+ AppContextUtil.getTraceId());

		EmployeeDetailsDTO employeeDetailsDTO = userAuthService.authoriseUser(reqDto);

		return AmxApiResponse.build(employeeDetailsDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getAllPermissions(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@PostMapping(value = ApiEndPoints.PERMS_GET)
	@ResponseBody
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(
			@RequestParam(required = true) String ipAddress, @RequestParam(required = false) String deviceId) {

		LOGGER.info("Received request for Get Permissions " + " from Ip Address: " + ipAddress + " from device Id: "
				+ deviceId + " TraceId: " + AppContextUtil.getTraceId());

		List<PermissionResposeDTO> permissionsResposeDTOList = userRoleService.getAllPermissions(ipAddress, deviceId);

		return AmxApiResponse.buildList(permissionsResposeDTOList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getAllRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	@ResponseBody
	@PostMapping(value = ApiEndPoints.ROLES_GET)
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(@RequestParam(required = true) String ipAddress,
			@RequestParam(required = false) String deviceId) {

		LOGGER.info("Received request for Get Roles " + " from Ip Address: " + ipAddress + " from device Id: "
				+ deviceId + " TraceId: " + AppContextUtil.getTraceId());

		List<RoleResponseDTO> rolesResponseDTOList = userRoleService.getAllRoles(ipAddress, deviceId);

		return AmxApiResponse.buildList(rolesResponseDTOList);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#saveRole(com.amx.jax.rbaac.dto.request.
	 * RoleRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.ROLES_SAVE, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(
			@RequestBody(required = true) @Valid RoleRequestDTO roleRequestDTO) {

		LOGGER.info("Received request for Save Roles " + " from Ip Address: " + roleRequestDTO.getIpAddr()
				+ " from device Id: " + roleRequestDTO.getDeviceId() + " TraceId: " + AppContextUtil.getTraceId());

		RoleResponseDTO rolesResponseDTO = userRoleService.saveRole(roleRequestDTO);

		return AmxApiResponse.build(rolesResponseDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#getUserRoleMappingsForBranch(java.math.
	 * BigDecimal, java.lang.String, java.lang.String)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.RA_GET_FOR_BRANCH, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(
			@RequestParam(required = true) BigDecimal countryBranchId, @RequestParam(required = true) String ipAddress,
			@RequestParam(required = false) String deviceId) {

		LOGGER.info("Received request for Get Role Allocations for Branch Users  " + " from Ip Address: " + ipAddress
				+ " from device Id: " + deviceId + " TraceId: " + AppContextUtil.getTraceId());

		UserRoleMappingsResponseDTO urMappingsResponseDTO = userRoleService
				.getUserRoleMappingsForBranch(countryBranchId, ipAddress, deviceId);

		return AmxApiResponse.build(urMappingsResponseDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
	 * request.UserRoleMappingsRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.RA_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(
			@RequestBody(required = true) @Valid UserRoleMappingsRequestDTO urmRequestDTO) {

		LOGGER.info("Received request for Update User Role Allocations " + " from Ip Address: "
				+ urmRequestDTO.getIpAddr() + " from device Id: " + urmRequestDTO.getDeviceId() + " TraceId: "
				+ AppContextUtil.getTraceId());

		List<UserRoleMappingDTO> urmDtoList = userRoleService.updateUserRoleMappings(urmRequestDTO);

		return AmxApiResponse.buildList(urmDtoList);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#updateEmployeeAccountDetails(com.amx.jax.
	 * rbaac .dto.request.EmployeeDetailsRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.UAC_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			@RequestBody(required = true) @Valid EmployeeDetailsRequestDTO edRequestDTO) {

		LOGGER.info("Received request for Update User Account Details " + " from Ip Address: "
				+ edRequestDTO.getIpAddr() + " from device Id: " + edRequestDTO.getDeviceId() + " TraceId: "
				+ AppContextUtil.getTraceId());

		List<EmployeeDetailsDTO> employeeDetailsDTOList = userAccountService.updateEmployee(edRequestDTO);

		return AmxApiResponse.buildList(employeeDetailsDTOList);
	}

	/**
	 * ******** APIs For Service Test ***********.
	 *
	 * @return the amx api response
	 */

	@Override
	@RequestMapping(value = ApiEndPoints.TEST_GET, method = RequestMethod.GET)
	public AmxApiResponse<String, Object> testGet() {

		String resp = respTestService.testGetUrlCall();

		return AmxApiResponse.build(resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#testPost()
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.TEST_POST, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> testPost() {

		return AmxApiResponse.build("Success");
	}

	@RequestMapping(value = ApiEndPoints.NOTP_VERIFY, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<NotpDTO, Object> verifyOTP(NotpDTO reqDTO) {
		reqDTO.setVerfied(userAuthService.validateOfflineOtp(reqDTO.getOtp(), reqDTO.getEmployeeId(), reqDTO.getSac()));
		return AmxApiResponse.build(reqDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#registerNewDevice(com.amx.jax.rbaac.dto.
	 * request.DeviceRegistrationRequest)
	 */
	@RequestMapping(value = ApiEndPoints.DEVICE_REG, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DeviceDto, Object> registerNewDevice(@Valid @RequestBody DeviceRegistrationRequest request) {

		DeviceDto newDevice = deviceService.registerNewDevice(request);
		return AmxApiResponse.build(newDevice);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#activateDevice(java.lang.Integer,
	 * java.lang.String)
	 */
	@RequestMapping(value = ApiEndPoints.DEVICE_ACTIVATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> activateDevice(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.MOTP, required = false) String mOtp) {
		BoolRespModel response = deviceService.activateDevice(deviceRegId);
		return AmxApiResponse.build(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#deactivateDevice(java.lang.Integer)
	 */
	@RequestMapping(value = ApiEndPoints.DEVICE_DEACTIVATE, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel, Object> deactivateDevice(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId) {
		BoolRespModel response = deviceService.deactivateDevice(deviceRegId);
		return AmxApiResponse.build(response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#createDeviceSession(java.lang.Integer,
	 * java.lang.String)
	 */
	@RequestMapping(value = ApiEndPoints.DEVICE_CREATE_SESSION, method = RequestMethod.POST)
	@Override
	public AmxApiResponse<DevicePairOtpResponse, Object> createDeviceSession(
			@RequestParam(name = Params.DEVICE_REG_ID) Integer deviceRegId,
			@RequestParam(name = Params.PAIRE_TOKEN) String paireToken) {
		DevicePairOtpResponse otpResponse = deviceService.sendOtpForPairing(deviceRegId, paireToken);
		return AmxApiResponse.build(otpResponse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#pairDeviceSession(com.amx.jax.dict.UserClient
	 * .ClientType, java.lang.Integer, java.lang.String)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.DEVICE_PAIR_SESSION, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, BoolRespModel> pairDeviceSession(
			@RequestParam(name = Params.DEVICE_TYPE) ClientType deviceType,
			@RequestParam(name = Params.TERMINAL_ID) Integer countryBranchSystemInventoryId,
			@RequestParam(name = Params.OTP) String otp) {
		DevicePairOtpResponse otpResponse = deviceService.validateOtpForPairing(deviceType,
				countryBranchSystemInventoryId, otp.toString());
		return AmxApiResponse.build(otpResponse, new BoolRespModel(Boolean.TRUE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.IRbaacService#validateDeviceSessionToken(java.math.
	 * BigDecimal, java.lang.String)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.DEVICE_VALIDATE_SESSION_TOKEN, method = RequestMethod.POST)
	public AmxApiResponse<DevicePairOtpResponse, Object> validateDeviceSessionToken(
			@RequestParam(name = Params.DEVICE_REG_ID) BigDecimal deviceRegId,
			@RequestParam(name = Params.SESSION_TOKEN) String deviceSessionToken) {
		DevicePairOtpResponse repsonse = deviceService.validateDeviceSessionPairToken(deviceRegId, deviceSessionToken);
		return AmxApiResponse.build(repsonse);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.IRbaacService#getDeviceRegIdByBranchInventoryId(com.amx.jax
	 * .dict.UserClient.ClientType, java.math.BigDecimal)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.DEVICE_GET_DEVICE_REG_ID, method = RequestMethod.POST)
	public AmxApiResponse<BigDecimal, Object> getDeviceRegIdByBranchInventoryId(
			@RequestParam(name = Params.DEVICE_CLIENT_TYPE) ClientType deviceClientType,
			@RequestParam(name = Params.DEVICE_SYS_INV_ID) BigDecimal countryBranchSystemInventoryId) {
		BigDecimal response = deviceService.getDeviceRegIdByBranchInventoryId(deviceClientType,
				countryBranchSystemInventoryId);
		return AmxApiResponse.build(response);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_ROLE_MAPPING_FOR_EMPLOYEE, method = RequestMethod.POST)
	public AmxApiResponse<RoleMappingForEmployee, Object> getRoleMappingsForEmployee(
			@RequestParam(required = true) BigDecimal employeeId, @RequestParam(required = true) String ipAddress,
			@RequestParam(required = false) String deviceId,
			@RequestParam(required = false, defaultValue = "false") Boolean filterRole) {
		LOGGER.info("In Get Role Mapping For Employee API...");

		RoleMappingForEmployee roleMappingForEmplyee = userRoleService.getRoleMappingsForEmployee(employeeId, ipAddress,
				deviceId, filterRole);

		return AmxApiResponse.build(roleMappingForEmplyee);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.EMPLOYEE_SYSTEM_MAPPING_CREATE, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> createEmployeeSystemMapping(
			@RequestParam(name = Params.EMPLOYEE_ID) BigDecimal employeeId,
			@RequestParam(name = Params.TERMINAL_ID) BigDecimal countryBranchSystemInventoryId) {
		BoolRespModel response = userAccountService.createEmployeeSystemMapping(employeeId,
				countryBranchSystemInventoryId);
		return AmxApiResponse.build(response);
	}

}
