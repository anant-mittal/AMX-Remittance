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
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rest.RestService;

/**
 * @author abhijeet
 *
 */
@Component
public class RbaacServiceClient implements RbaacService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(UserAuthInitReqDTO userAuthInitReqDTO) {

		LOGGER.info("Init Auth Request called for Employee No: {}, Identity: {}, from IP address: {}",
				userAuthInitReqDTO.getEmployeeNo(), userAuthInitReqDTO.getIdentity(),
				userAuthInitReqDTO.getIpAddress());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.INIT_AUTH).post(userAuthInitReqDTO)
				.asApiResponse(UserAuthInitResponseDTO.class);

	}

	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(UserAuthorisationReqDTO reqDTO) {

		LOGGER.info("Authorisation Request called for Employee No: {}, from IP address: {}", reqDTO.getEmployeeNo(),
				reqDTO.getIpAddress());

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.AUTHORISE).post(reqDTO)
				.asApiResponse(EmployeeDetailsDTO.class);

	}

}
