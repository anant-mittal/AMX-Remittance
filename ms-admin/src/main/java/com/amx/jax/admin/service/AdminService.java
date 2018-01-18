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
		logger.debug("inside AdminService : unlockCustomer");
		ApiResponse<BooleanResponse> response = userClient.unLockCustomer();
		return response;
	}
	
    public ApiResponse<BooleanResponse> deactivateCustomer(String civilId) {
    	logger.debug("inside AdminService : deactivateCustomer");
		ApiResponse<BooleanResponse> response = userClient.deActivateCustomer();
		return response;
	}
    
    public ApiResponse<BooleanResponse> createorUpdateOtpSettings(OtpSettings otpSettings) {	
    	logger.debug("inside AdminService : createorUpdateOtpSettings");
		ApiResponse<BooleanResponse> response = jaxConfigClient.createorUpdateOtpSettings(otpSettings);
		return response;
	}

}
