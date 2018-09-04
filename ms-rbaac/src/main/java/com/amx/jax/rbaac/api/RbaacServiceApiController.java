/**
 * 
 */
package com.amx.jax.rbaac.api;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.dto.request.RoleRequestDTO;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionResposeDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingsResponseDTO;
import com.amx.jax.rbaac.service.RespTestService;
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

	@Autowired
	RespTestService respTestService;

	@Autowired
	UserRoleService userRoleService;

	/**
	 * Init User Authentication.
	 *
	 * @param employeeNo
	 *            the employee no
	 * @param identity
	 *            the identity
	 * @param ipAddress
	 *            the ip address
	 * @return the amx api response
	 * 
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(
			@RequestBody UserAuthInitReqDTO userAuthInitReqDTO) {

		LOGGER.info("Begin Init Auth for User: " + userAuthInitReqDTO.getEmployeeNo() + " from Ip Address: "
				+ userAuthInitReqDTO.getIpAddress());
		UserAuthInitResponseDTO userAuthInitResponseDTO = userAuthService.verifyUserDetails(userAuthInitReqDTO);

		return AmxApiResponse.build(userAuthInitResponseDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.AUTHORISE, method = RequestMethod.POST)
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(@RequestBody UserAuthorisationReqDTO reqDto) {

		LOGGER.info("Received request for authorising User Access : " + reqDto.getEmployeeNo() + " from Ip Address: "
				+ reqDto.getIpAddress() + " from device Id: " + reqDto.getDeviceId());

		EmployeeDetailsDTO employeeDetailsDTO = userAuthService.authoriseUser(reqDto);

		return AmxApiResponse.build(employeeDetailsDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.PERMS_GET, method = RequestMethod.POST)
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(@RequestParam(required = true) String ipAddr,
			@RequestParam String deviceId) {

		LOGGER.info("Received request for Get Permissions " + " from Ip Address: " + ipAddr + " from device Id: "
				+ deviceId);

		List<PermissionResposeDTO> permissionsResposeDTOList = userRoleService.getAllPermissions(ipAddr, deviceId);

		return AmxApiResponse.buildList(permissionsResposeDTOList);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.ROLES_GET, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(@RequestParam(required = true) String ipAddr,
			@RequestParam String deviceId) {

		LOGGER.info("Received request for Get Roles " + " from Ip Address: " + ipAddr + " from device Id: " + deviceId);

		List<RoleResponseDTO> rolesResponseDTOList = userRoleService.getAllRoles(ipAddr, deviceId);

		return AmxApiResponse.buildList(rolesResponseDTOList);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.ROLES_SAVE, method = RequestMethod.POST)
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(
			@RequestBody(required = true) RoleRequestDTO roleRequestDTO) {

		LOGGER.info("Received request for Save Roles " + " from Ip Address: " + roleRequestDTO.getIpAddr()
				+ " from device Id: " + roleRequestDTO.getDeviceId());

		RoleResponseDTO rolesResponseDTO = userRoleService.saveRole(roleRequestDTO);

		return AmxApiResponse.build(rolesResponseDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.RA_GET_FOR_BRANCH, method = RequestMethod.POST)
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(
			@RequestParam(required = true) BigDecimal countryBranchId, @RequestParam(required = true) String ipAddr,
			@RequestParam String deviceId) {

		LOGGER.info("Received request for Get Role Allocations for Branch Users  " + " from Ip Address: " + ipAddr
				+ " from device Id: " + deviceId);

		UserRoleMappingsResponseDTO urMappingsResponseDTO = userRoleService
				.getUserRoleMappingsForBranch(countryBranchId, ipAddr, deviceId);

		return AmxApiResponse.build(urMappingsResponseDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.TEST_GET, method = RequestMethod.GET)
	public AmxApiResponse<String, Object> testGet() {

		String resp = respTestService.testGetUrlCall();

		return AmxApiResponse.build(resp);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.TEST_POST, method = RequestMethod.POST)
	public AmxApiResponse<String, Object> testPost() {

		return AmxApiResponse.build("Success");
	}

}
