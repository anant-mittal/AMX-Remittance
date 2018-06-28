package com.amx.jax.auth.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.auth.AuthConstants;
import com.amx.jax.auth.AuthService;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rest.RestService;

@Component
public class AuthClient implements AuthService {

	private static final Logger LOGGER = LoggerService.getLogger(AuthClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public String syncPermsMeta() throws PostManException {
		return restService.ajax(appConfig.getAuthURL()).path(AuthConstants.ApiEndPoints.SYNC_PERMS).post()
				.as(String.class);

	}
}
