package com.amx.jax.controller;

import static com.amx.jax.constant.ApiEndpoint.CUSTOMER_ENDPOINT;

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
public class CustomerController {

	@Autowired
	private ConverterUtil converterUtil;

	@Autowired
	private UserService userSerivce;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse saveCust(@RequestBody String json) {
		CustomerModel model = (CustomerModel) converterUtil.unmarshall(json, CustomerModel.class);
		ApiResponse response = userSerivce.saveCustomer(model);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/send-otp/", method = RequestMethod.GET)
	public ApiResponse verifyCivilId(@PathVariable("civil-id") String civilId) {
		ApiResponse response = userSerivce.sendOtpForCivilId(civilId);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/validate-otp/", method = RequestMethod.GET)
	public ApiResponse validateOtp(@PathVariable("civil-id") String civilId, @RequestParam("otp") String otp) {
		ApiResponse response = userSerivce.validateOtp(civilId, otp);
		return response;
	}

	@RequestMapping(value = "/{civil-id}/checklist/", method = RequestMethod.GET)
	public ApiResponse getCheckList(@PathVariable("civil-id") String civilId) {
		ApiResponse response = userSerivce.getUserCheckList(civilId);
		return response;
	}
}
