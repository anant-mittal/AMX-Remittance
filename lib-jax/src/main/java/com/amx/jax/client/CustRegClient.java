package com.amx.jax.client;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.ICustRegService;
import com.amx.jax.JaxConstants;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rest.RestService;

@Component
public class CustRegClient implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public String syncPermsMeta() throws PostManException {
		return restService.ajax(appConfig.getAuthURL()).path(JaxConstants.CustRegApiEndPoints.GET_ID_FIELDS).post()
				.as(String.class);

	}
}
