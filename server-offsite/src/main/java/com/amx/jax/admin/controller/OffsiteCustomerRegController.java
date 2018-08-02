package com.amx.jax.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.admin.service.EmployeeService;
import com.amx.jax.model.AuthData;
import com.amx.jax.response.ResponseWrapper;

import io.swagger.annotations.Api;

@RestController
@Api(value = "OffsiteCustomerRegistration APIs")
public class OffsiteCustomerRegController {
	
	@Autowired
	EmployeeService employeeService;

	@RequestMapping(value = "/pub/offsiteregister/auth/otp", method = { RequestMethod.POST })
	public ResponseWrapper<AuthData> sendOTP(@RequestBody OffsiteCustomerRegistrationRequest offsiteCustRegModel) {		
		return employeeService.sendOTP(offsiteCustRegModel);
	}
}
