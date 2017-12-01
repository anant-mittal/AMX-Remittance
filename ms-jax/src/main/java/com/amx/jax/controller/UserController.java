package com.amx.jax.controller;

import static com.amx.jax.constant.ApiEndpoint.USER_API_ENDPOINT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.userservice.service.UserService;

@RestController
@RequestMapping(USER_API_ENDPOINT)
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse loginUser(@RequestParam String userId, @RequestParam String password) {

		ApiResponse response = userService.loginUser(userId, password);
		return response;
	}

}