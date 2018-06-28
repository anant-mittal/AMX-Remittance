package com.amx.jax.auth.service;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.amx.jax.auth.AuthService;
import com.amx.jax.logger.LoggerService;

@Component
public class AuthServiceImpl implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthServiceImpl.class);

}
