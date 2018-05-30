package com.amx.jax.auth.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.auth.service.AuthServiceImpl;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.GeoLocation;

import io.swagger.annotations.ApiParam;

@RestController
public class AuthServiceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceController.class);

	@Autowired
	AuthServiceImpl authServiceImpl;

	@RequestMapping(value = PostManUrls.GEO_LOC, method = RequestMethod.GET)
	public GeoLocation geoLocation(@RequestParam @ApiParam(defaultValue = "171.50.210.63") String ip)
			throws PostManException, IOException {
		return null;
	}

}
