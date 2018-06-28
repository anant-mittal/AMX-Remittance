package com.amx.jax.auth.service;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.service.AuthService;

@Component
public class AuthServiceImpl implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceImpl.class);

	public AuthServiceImpl() {
	}

	@Value("${default.tenant}")
	String defaultTennatId;


}
