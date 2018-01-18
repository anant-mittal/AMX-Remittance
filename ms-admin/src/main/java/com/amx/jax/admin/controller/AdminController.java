/**  AlMulla Exchange
  *  
  */
package com.amx.jax.admin.controller;

import static com.amx.jax.admin.constant.AdminConstant.ADMIN_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.admin.service.AdminService;
import com.amx.jax.amxlib.config.OtpSettings;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@Controller
@RequestMapping(ADMIN_API_ENDPOINT)
@Api(value = "Admin APIs")
@SuppressWarnings("rawtypes")
public class AdminController {

	private Logger logger = Logger.getLogger(AdminController.class);
	
	@Autowired
	private AdminService adminService;
	
	
	@RequestMapping(value = "/customer/unlock/{civilid}", method = RequestMethod.GET)
	public ApiResponse unlockCustomer(@PathVariable("civilid") String civilid) {
		logger.debug("in unlockCustomer Request ");
		ApiResponse response = adminService.unlockCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/customer/deactivate/{civilid}", method = RequestMethod.GET)
	public ApiResponse deActivateCustomer(@PathVariable("civilid") String civilid) {
		logger.debug("in deActivateCustomer Request ");
		ApiResponse response = adminService.deactivateCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ApiResponse createorUpdateOtpSettings(@RequestParam Integer maxValidateOtpAttempts,
												 @RequestParam Integer maxSendOtpAttempts,
												 @RequestParam Integer otpValidityTime	) {
		logger.debug("in createorUpdateOtpSettings Request ");
		
		ApiResponse response = adminService.createorUpdateOtpSettings(new OtpSettings(maxValidateOtpAttempts,maxSendOtpAttempts,otpValidityTime));
		return response;
	}

}
