/**  AlMulla Exchange
  *  
  */
package com.amx.jax.admin.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.client.JaxConfigClient;
import com.amx.jax.client.UserClient;

@Component
public class AdminService {

	private static Logger logger = Logger.getLogger(AdminService.class);
	
	@Autowired
	private UserClient userClient;
	
	@Autowired
	private JaxConfigClient jaxConfigClient;
	
	public ApiResponse<BooleanResponse> unlockCustomer(String civilId) {
		logger.info("inside AdminService : unlockCustomer fro civilId : "+civilId);
		ApiResponse<BooleanResponse> response = userClient.unLockCustomer(civilId);
		return response;
	}
	
    public ApiResponse<BooleanResponse> deactivateCustomer(String civilId) {
    	logger.info("inside AdminService : deactivateCustomer for civilId : "+civilId);
		ApiResponse<BooleanResponse> response = userClient.deActivateCustomer();
		return response;
	}
    
    public ApiResponse<BooleanResponse> createorUpdateOtpSettings(OtpSettings otpSettings) {	
    	logger.info(String.format("inside AdminService : createorUpdateOtpSettings maxAttempts : %s, maxValidateAttempts : %s, validityTime : %s",otpSettings.getMaxSendOtpAttempts(),otpSettings.getMaxValidateOtpAttempts(),otpSettings.getOtpValidityTime()));
		ApiResponse<BooleanResponse> response = jaxConfigClient.createorUpdateOtpSettings(otpSettings);
		return response;
	}

}
