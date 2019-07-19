package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import java.math.BigDecimal;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.constant.ApiEndpoint.UserApi;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.service.ICustomerService.Params;
import com.amx.amxlib.service.ICustomerService.Path;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.FingerprintService;
import com.amx.jax.customer.service.JaxCustomerContactVerificationService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.utils.Constants;

@RestController
@RequestMapping(USER_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private FingerprintService fingerprintService;
	
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	UserValidationService userValidationService;
	
	@Autowired
	JaxCustomerContactVerificationService jaxCustomerContactVerificationService;
	
	

	private Logger logger = LoggerService.getLogger(UserController.class);

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ApiResponse loginUser(@RequestBody CustomerModel customerModel) {
		logger.info("loginUser Request: usreid: " + customerModel.getLoginId());
		//jaxCustomerContactVerificationService.validateEmailVerification(customerModel.getLoginId());
		jaxCustomerContactVerificationService.validateEmailVerification(customerModel.getLoginId());
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

	

	@RequestMapping(value = UserApi.LINK_DEVICE_LOGGEDIN_USER, method = RequestMethod.POST)
	public AmxApiResponse<UserFingerprintResponseModel, Object> linkDeviceId() {
		logger.debug("metaData.getCustomerId() : {}", metaData.getCustomerId());
		UserFingerprintResponseModel userFingerprintResponseModel = fingerprintService.linkDeviceId(metaData.getCustomerId());
		return AmxApiResponse.build(userFingerprintResponseModel);
	}

	@RequestMapping(value = UserApi.LOGIN_CUSTOMER_BY_FINGERPRINT, method = RequestMethod.POST)
	public AmxApiResponse<CustomerModel, Object> loginCustomerByFingerprint(@RequestParam(value = UserApi.IDENTITYINT) String identityInt,
			@RequestParam(required=false) String identityType,
			@RequestParam(value = UserApi.PASSWORD) String password) {
		logger.debug(MessageFormat.format("IdentityInt value is {0} :", identityInt));
		logger.debug(MessageFormat.format("IdentityType value is {0} :", identityType));
		// Validate TODO:- @Anant
		jaxCustomerContactVerificationService.validateEmailVerification(identityInt);
		CustomerModel customerModel = fingerprintService.loginCustomerByFingerprint(identityInt, identityType, password,
				metaData.getDeviceId());

		return AmxApiResponse.build(customerModel);
	}
	
	@RequestMapping(value = UserApi.DELINK_FINGERPRINT, method = RequestMethod.POST)
	public  BoolRespModel delinkFingerprint() {
		return fingerprintService.delinkFingerprint();
	}
	
	@RequestMapping(value = UserApi.RESET_FINGERPRINT, method = RequestMethod.POST)
	public BoolRespModel resetFingerprint(@RequestParam(value = UserApi.IDENTITYINT) String identity,
			@RequestParam(defaultValue = Constants.IDENTITY_TYPE_CIVIL_ID_STR) String identityType) {
		return fingerprintService.resetFingerprint(identity, identityType);
	}
	@RequestMapping(value = Path.RESEND_EMAIL_LOGIN, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> sendEmailOnLogin(@RequestBody CustomerModel customerModel){
		jaxCustomerContactVerificationService.sendEmailVerifyLinkOnReg(customerModel);
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return AmxApiResponse.build(boolRespModel);
	}
}
