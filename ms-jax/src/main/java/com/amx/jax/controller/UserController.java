package com.amx.jax.controller;

import static com.amx.amxlib.constant.ApiEndpoint.USER_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.userservice.service.UserService;

@RestController
@RequestMapping(USER_API_ENDPOINT)
@SuppressWarnings("rawtypes")
public class UserController {

	@Autowired
	private UserService userService;

	private Logger logger = Logger.getLogger(UserController.class);

	@RequestMapping(value = "/login/", method = RequestMethod.POST)
	public ApiResponse loginUser(@RequestParam String userId, @RequestParam String password) {
		logger.debug("loginUser Request: usreid: " + userId + " pssword: " + password);
		ApiResponse response = userService.loginUser(userId, password);
		return response;
	}

}
