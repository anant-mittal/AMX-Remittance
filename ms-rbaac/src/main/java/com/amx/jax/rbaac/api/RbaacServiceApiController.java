/**
 * 
 */
package com.amx.jax.rbaac.api;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.request.UserRoleMappingsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingsResponseDTO;
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
public class RbaacServiceApiController implements RbaacService {

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

	/**
	 * Init User Authentication.
	 *
	 * @param userAuthInitReqDTO
	 *            the user auth init req DTO
	 * @return the amx api response
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(
			@RequestBody UserAuthInitReqDTO userAuthInitReqDTO) {

		LOGGER.info("Begin Init Auth for User: " + userAuthInitReqDTO.getEmployeeNo() + " from Ip Address: "
				+ userAuthInitReqDTO.getIpAddress() + " from device Id: " + userAuthInitReqDTO.getDeviceId()
				+ " with TraceId: " + AppContextUtil.getTraceId());
		UserAuthInitResponseDTO userAuthInitResponseDTO = userAuthService.verifyUserDetails(userAuthInitReqDTO);

		return AmxApiResponse.build(userAuthInitResponseDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.AUTHORISE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(@RequestBody UserAuthorisationReqDTO reqDto) {

		LOGGER.info("Received request for authorising User Access : " + reqDto.getEmployeeNo() + " from Ip Address: "
				+ reqDto.getIpAddress() + " from device Id: " + reqDto.getDeviceId() + " TraceId: "
				+ AppContextUtil.getTraceId());

		EmployeeDetailsDTO employeeDetailsDTO = userAuthService.authoriseUser(reqDto);

		return AmxApiResponse.build(employeeDetailsDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllPermissions(java.lang.String,
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
	 * @see com.amx.jax.rbaac.RbaacService#getAllRoles(java.lang.String,
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
	 * @see com.amx.jax.rbaac.RbaacService#saveRole(com.amx.jax.rbaac.dto.request.
	 * RoleRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.ROLES_SAVE, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(
			@RequestBody(required = true) RoleRequestDTO roleRequestDTO) {

		LOGGER.info("Received request for Save Roles " + " from Ip Address: " + roleRequestDTO.getIpAddr()
				+ " from device Id: " + roleRequestDTO.getDeviceId() + " TraceId: " + AppContextUtil.getTraceId());

		RoleResponseDTO rolesResponseDTO = userRoleService.saveRole(roleRequestDTO);

		return AmxApiResponse.build(rolesResponseDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getUserRoleMappingsForBranch(java.math.
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
	 * com.amx.jax.rbaac.RbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
	 * request.UserRoleMappingsRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.RA_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(
			@RequestBody(required = true) UserRoleMappingsRequestDTO urmRequestDTO) {

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
	 * com.amx.jax.rbaac.RbaacService#updateEmployeeAccountDetails(com.amx.jax.rbaac
	 * .dto.request.EmployeeDetailsRequestDTO)
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.UAC_UPDATE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			@RequestBody(required = true) EmployeeDetailsRequestDTO edRequestDTO) {

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
	 * @see com.amx.jax.rbaac.RbaacService#testPost()
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.TEST_POST, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> testPost() {

		return AmxApiResponse.build("Success");
	}

}
