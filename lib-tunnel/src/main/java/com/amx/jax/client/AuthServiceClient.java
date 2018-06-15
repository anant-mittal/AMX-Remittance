package com.amx.jax.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.AuthService;

@Component
public class AuthServiceClient implements AuthService {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

}
