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
import com.amx.jax.model.dto.SendOtpModel;
import com.amx.jax.rbaac.RbaacService;
import com.amx.jax.rbaac.service.AuthServiceImpl;

/**
 * @author abhijeet
 *
 */
@RestController
public class RbaacServiceApiController implements RbaacService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	AuthServiceImpl authService;
	
	/**
	 * 
	 */
	@Override
	@RequestMapping(value = ApiEndPoints.INIT_AUTH, method = RequestMethod.POST)
	public AmxApiResponse<SendOtpModel, Object> initAuthForEmployee(@RequestParam String empCode, 
			@RequestParam String identity, @RequestParam String ipAddress) {
		
		
		
		LOGGER.info("Begin Init Auth for Employee: " + empCode);
		return authService.verifyUserDetails(empCode, identity, ipAddress);
	}

	
}
