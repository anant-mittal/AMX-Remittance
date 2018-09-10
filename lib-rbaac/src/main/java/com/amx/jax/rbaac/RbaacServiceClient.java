/**
 * 
 */
package com.amx.jax.rbaac;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
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
import com.amx.jax.rest.RestService;

/**
 * The Class RbaacServiceClient.
 *
 * @author abhijeet
 */
@Component
public class RbaacServiceClient implements RbaacService {

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
	 * @see
	 * com.amx.jax.rbaac.RbaacService#initAuthForUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthInitReqDTO)
	 */
	@Override
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO) {

		LOGGER.info("Init Auth Request called for Employee No: {}, Identity: {}, from IP address: {}",
				userAuthInitReqDTO.getEmployeeNo(), userAuthInitReqDTO.getIdentity(),
				userAuthInitReqDTO.getIpAddress());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.INIT_AUTH).post(userAuthInitReqDTO)
				.asApiResponse(UserAuthInitResponseDTO.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#authoriseUser(com.amx.jax.rbaac.dto.request.
	 * UserAuthorisationReqDTO)
	 */
	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDTO) {

		LOGGER.info("Authorisation Request called for Employee No: {}, from IP address: {}", reqDTO.getEmployeeNo(),
				reqDTO.getIpAddress());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.AUTHORISE).post(reqDTO)
				.asApiResponse(EmployeeDetailsDTO.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllPermissions(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<PermissionResposeDTO, Object> getAllPermissions(String ipAddress, String deviceId) {

		LOGGER.info("Received request for User Permissions, from IP address: {}, device Id: {}", ipAddress, deviceId);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.PERMS_GET).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post().asApiResponse(PermissionResposeDTO.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<RoleResponseDTO, Object> getAllRoles(String ipAddress, String deviceId) {

		LOGGER.info("Received request for User Roles, from IP address: {}, device Id: {}", ipAddress, deviceId);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLES_GET).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post().asApiResponse(RoleResponseDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#saveRole(com.amx.jax.rbaac.dto.request.
	 * RoleRequestDTO)
	 * 
	 */
	@Override
	public AmxApiResponse<RoleResponseDTO, Object> saveRole(RoleRequestDTO roleRequestDTO) {

		LOGGER.info("Received request for Save Role, from IP address: {}, device Id: {}", roleRequestDTO.getIpAddr(),
				roleRequestDTO.getDeviceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLES_SAVE).post(roleRequestDTO)
				.asApiResponse(RoleResponseDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getUserRoleMappingsForBranch(java.math.
	 * BigDecimal, java.lang.String, java.lang.String)
	 */
	@Override
	public AmxApiResponse<UserRoleMappingsResponseDTO, Object> getUserRoleMappingsForBranch(BigDecimal countryBranchId,
			String ipAddress, String deviceId) {

		LOGGER.info(
				"Received request for Get User Role Allocations For Branch Id: {} , from IP address: {}, device Id: {}",
				countryBranchId, ipAddress, deviceId);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.RA_GET_FOR_BRANCH)
				.queryParam("countryBranchId", countryBranchId).queryParam("ipAddress", ipAddress)
				.queryParam("deviceId", deviceId).post().asApiResponse(UserRoleMappingsResponseDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#updateUserRoleMappings(com.amx.jax.rbaac.dto.
	 * request.UserRoleMappingsRequestDTO)
	 */
	@Override
	public AmxApiResponse<UserRoleMappingDTO, Object> updateUserRoleMappings(UserRoleMappingsRequestDTO urmRequestDTO) {

		LOGGER.info("Received request for Update User Role Allocations " + " from Ip Address: "
				+ urmRequestDTO.getIpAddr() + " from device Id: " + urmRequestDTO.getDeviceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.RA_UPDATE).post(urmRequestDTO)
				.asApiResponse(UserRoleMappingDTO.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.rbaac.RbaacService#updateEmployeeAccountDetails(com.amx.jax.rbaac
	 * .dto.request.EmployeeDetailsRequestDTO)
	 */
	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> updateEmployeeAccountDetails(
			EmployeeDetailsRequestDTO edRequestDTO) {

		LOGGER.info("Received request for Update User Account Details " + " from Ip Address: "
				+ edRequestDTO.getIpAddr() + " from device Id: " + edRequestDTO.getDeviceId());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.UAC_UPDATE).post(edRequestDTO)
				.asApiResponse(EmployeeDetailsDTO.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#testGet()
	 */
	@Override
	public AmxApiResponse<String, Object> testGet() {
		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.TEST_GET).get().asApiResponse(String.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#testPost()
	 */
	@Override
	public AmxApiResponse<String, Object> testPost() {
		// TODO Auto-generated method stub
		return null;
	}

}
