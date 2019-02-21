package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;
import static com.amx.amxlib.constant.ApiEndpoint.LINK_DEVICE_LOGGEDIN_CUSTOMER;
import static com.amx.amxlib.constant.ApiEndpoint.LINK_DEVICEID;
import static com.amx.amxlib.constant.ApiEndpoint.LOGIN_CUSTOMER_BY_FINGERPRINT;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.UserIdentityModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.utils.Constants;
import com.amx.utils.Random;

import io.swagger.annotations.ApiParam;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(USER_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class UserController {

	@Autowired
	private UserService userService;
	
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	UserValidationService userValidationService;
	
	
	
	
	

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

	@RequestMapping(value = LINK_DEVICEID, method = RequestMethod.POST)
	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceId(@RequestParam String identityInt,
			@ApiIgnore @ApiParam(hidden = true) @RequestParam(defaultValue = Constants.IDENTITY_TYPE_CIVIL_ID_STR) String identityType) {
		logger.debug(MessageFormat.format("IdentityInt value is {0} :", identityInt));
		logger.debug(MessageFormat.format("IdentityType value is {0} :", identityType));

		UserFingerprintResponseModel userFingerprintResponseModel = userService.linkDeviceId(identityInt, identityType);
		return AmxApiResponse.build(userFingerprintResponseModel);

	}

	@RequestMapping(value = LINK_DEVICE_LOGGEDIN_CUSTOMER, method = RequestMethod.POST)
	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceId() {
		UserFingerprintResponseModel userFingerprintResponseModel = userService.linkDeviceId(metaData.getCustomerId());
		return AmxApiResponse.build(userFingerprintResponseModel);
	}

	@RequestMapping(value = LOGIN_CUSTOMER_BY_FINGERPRINT, method = RequestMethod.POST)
	public AmxApiResponse<CustomerModel, Object> loginCustomerByFingerprint(@RequestParam String identityInt,
			@RequestParam(defaultValue = Constants.IDENTITY_TYPE_CIVIL_ID_STR) String identityType, @RequestParam String password) {
		logger.debug(MessageFormat.format("IdentityInt value is {0} :", identityInt));
		logger.debug(MessageFormat.format("IdentityType value is {0} :", identityType));
		CustomerModel customerModel = userService.loginCustomerByFingerprint(identityInt, identityType, password);
		return AmxApiResponse.build(customerModel);
	}

}
