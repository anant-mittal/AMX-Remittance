/**
 * 
 */
package com.amx.jax.rbaac.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.dto.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.service.UserAuthService;

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
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<UserAuthInitResponseDTO, Object> initAuthForUser(@RequestParam String employeeNo,
			@RequestParam String identity, @RequestParam String ipAddress) {

		LOGGER.info("Begin Init Auth for User: " + employeeNo);
		UserAuthInitResponseDTO userAuthInitResponseDTO = userAuthService.verifyUserDetails(employeeNo, identity,
				ipAddress);

		return AmxApiResponse.build(userAuthInitResponseDTO);
	}

}
