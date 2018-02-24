package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.UserService;

@RestController
@RequestMapping(USER_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@Autowired
	MetaData metaData;

	private Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ApiResponse loginUser(@RequestBody CustomerModel customerModel) {
		logger.info("loginUser Request: usreid: " + customerModel.getLoginId());
		ApiResponse response = userService.loginUser(customerModel.getLoginId(), customerModel.getPassword());
		return response;
	}
	
	
	@RequestMapping(value = "/myprofile-info/", method = RequestMethod.GET)
	public ApiResponse getMyInfo() {
		BigDecimal customerId = metaData.getCustomerId();
		BigDecimal companyId  = metaData.getCompanyId();
		BigDecimal countryId  = metaData.getCountryId();
		ApiResponse response = userService.getCustomerInfo(countryId, companyId, customerId);
		return response;
	}

}
