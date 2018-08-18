/**
 * 
 */
package com.amx.jax.rbaac;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dto.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.UserAuthInitResponseDTO;
import com.amx.jax.rest.RestService;

/**
 * @author abhijeet
 *
 */
public class RbaacServiceClient implements RbaacService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(String employeeNo, String identity,
			String ipAddress) {

		LOGGER.info("Init Auth Request called for Employee No: {}, Identity: {}, from IP address: {}", employeeNo,
				identity, ipAddress);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.INIT_AUTH)
				.queryParam("employeeNo", employeeNo).queryParam("identity", identity)
				.queryParam("ipAddress", ipAddress).get().asApiResponse(UserAuthInitResponseDTO.class);

	}

	@Override
	public AmxApiResponse<EmployeeDetailsDTO, Object> authoriseUser(String employeeNo, String mOtpHash, String eOtpHash,
			String ipAddress) {

		LOGGER.info("Authorisation Request called for Employee No: {}, from IP address: {}", employeeNo, ipAddress);

		return restService.ajax(appConfig.getAuthURL()).path(ApiEndPoints.AUTHORISE)
				.queryParam("employeeNo", employeeNo).queryParam("mOtpHash", mOtpHash).queryParam("eOtpHash", eOtpHash)
				.queryParam("ipAddress", ipAddress).get().asApiResponse(EmployeeDetailsDTO.class);

	}

}
