package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(CUSTOMER_ENDPOINT)
@SuppressWarnings("rawtypes")
public class CustomerController {

	@Autowired
	private ConverterUtil converterUtil;

	@Autowired
	private UserService userSerivce;

	private Logger logger = Logger.getLogger(UserService.class);

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse saveCust(@RequestBody String json) {
		logger.debug("saveCust Request:" + json);
		CustomerModel model = (CustomerModel) converterUtil.unmarshall(json, CustomerModel.class);
		ApiResponse response = userSerivce.saveCustomer(model);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/send-otp/", method = RequestMethod.GET)
	public ApiResponse verifyCivilId(@PathVariable("civil-id") String civilId) {
		logger.debug("verifyCivilId Request:civilId" + civilId);
		ApiResponse response = userSerivce.sendOtpForCivilId(civilId);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/validate-otp/", method = RequestMethod.GET)
	public ApiResponse validateOtp(@PathVariable("civil-id") String civilId, @RequestParam("otp") String otp) {
		logger.debug("validateOtp Request:civilId" + civilId + " otp:" + otp);
		ApiResponse response = userSerivce.validateOtp(civilId, otp);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/checklist/", method = RequestMethod.GET)
	public ApiResponse getCheckList(@PathVariable("civil-id") String civilId) {
		logger.debug("getCheckList Request:civilId" + civilId);
		ApiResponse response = userSerivce.getUserCheckList(civilId);
		return response;
	}

	@RequestMapping(value = "{customer-id}/random-questions/", method = RequestMethod.GET)
	public ApiResponse getRandomQuestion(@RequestParam Integer size, @PathVariable("customer-id") Integer customerId) {
		logger.debug("getCheckList Request: customerId" + customerId + " size= " + size);
		ApiResponse response = userSerivce.generateRandomQuestions(size, customerId);
		return response;
	}

	@RequestMapping(value = "/validate-random-questions/", method = RequestMethod.POST)
	public ApiResponse validateCustomerData(@RequestBody String json) {
		logger.debug("validateCustomerData Request:" + json);
		CustomerModel model = (CustomerModel) converterUtil.unmarshall(json, CustomerModel.class);
		ApiResponse response = userSerivce.validateCustomerData(model);
		return response;
	}

	@RequestMapping(value = "/{user-id}/password/", method = RequestMethod.PUT)
	public ApiResponse updatePassword(@PathParam("user-id") Integer customerId, @RequestParam String password) {
		logger.debug("updatePassword Request:  pssword: " + password);
		ApiResponse response = userSerivce.updatePassword(customerId, password);
		return response;
	}
}
