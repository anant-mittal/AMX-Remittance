package com.amx.jax.userservice.controller;

import static com.amx.jax.constant.ApiEndpoint.USER_API_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.ConverterUtil;

@RestController
@RequestMapping(USER_API_ENDPOINT)
public class UserController {

	@Autowired
	private MetaData metaData;

	@Autowired
	private ConverterUtil converterUtil;
	
	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse registerUser(@RequestBody String json) {

		UserModel userModel = (UserModel) converterUtil.unmarshall(json, userService.getModelClass());
		ApiResponse response = userService.registerUser(userModel);

		return response;
	}

}
