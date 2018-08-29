/**
 * 
 */
package com.amx.jax.rbaac;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.PermissionsResposeDTO;
import com.amx.jax.rbaac.dto.response.RolesResponseDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
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
	public AmxApiResponse<PermissionsResposeDTO, Object> getAllPermissions(String ipAddr, String deviceId) {

		LOGGER.info("Received request for User Permissions, from IP address: {}, device Id: {}", ipAddr, deviceId);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.PERMS_GET).queryParam("ipAddress", ipAddr)
				.queryParam("deviceId", deviceId).post().asApiResponse(PermissionsResposeDTO.class);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.rbaac.RbaacService#getAllRoles(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public AmxApiResponse<RolesResponseDTO, Object> getAllRoles(String ipAddr, String deviceId) {

		LOGGER.info("Received request for User Roles, from IP address: {}, device Id: {}", ipAddr, deviceId);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.ROLES_GET).queryParam("ipAddress", ipAddr)
				.queryParam("deviceId", deviceId).post().asApiResponse(RolesResponseDTO.class);
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
