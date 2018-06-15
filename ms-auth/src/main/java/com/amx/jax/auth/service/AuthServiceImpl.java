package com.amx.jax.auth.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.service.AuthService;

@Component
public class AuthServiceImpl implements AuthService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);

	public AuthServiceImpl() {
	}

	@Value("${default.tenant}")
	String defaultTennatId;


}
