package com.amx.jax.branch.controller;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.sso.SSOUser;

import io.swagger.annotations.Api;

@RestController
@Api(value = "User Meta APIs")
public class UserMetaController {

	private Logger logger = Logger.getLogger(UserMetaController.class);

	@Autowired
	private SSOUser ssoUser;

	@RequestMapping(value = "/pub/meta", method = RequestMethod.GET)
	public AmxApiResponse<EmployeeDetailsDTO, Object> listOfTenants()
			throws PostManException, InterruptedException, ExecutionException {
		return AmxApiResponse.build(ssoUser.getUserDetails());
	}

}
