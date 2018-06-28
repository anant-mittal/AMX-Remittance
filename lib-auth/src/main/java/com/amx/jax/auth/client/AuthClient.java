package com.amx.jax.auth.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.auth.AuthService;
import com.amx.jax.logger.LoggerService;

@Component
public class AuthClient implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthClient.class);

	public AuthClient() {
	}

	@Value("${default.tenant}")
	String defaultTennatId;

}
