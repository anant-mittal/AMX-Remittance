/**
 * 
 */
package com.amx.jax.rbaac.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.dto.request.UserAuthInitReqDTO;
import com.amx.jax.rbaac.dto.request.UserAuthorisationReqDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.UserAuthInitResponseDTO;
import com.amx.jax.rbaac.service.RespTestService;
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

	@Autowired
	RespTestService respTestService;

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
