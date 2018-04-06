package com.amx.jax.postman.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.jax.postman.service.GeoLocationServiceImpl;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import io.swagger.annotations.ApiParam;

@RestController
public class GeoServiceController {

	private static final Logger LOGGER = LoggerFactory.getLogger(GeoServiceController.class);

	@Autowired
	GeoLocationServiceImpl geoLocationService;

	@RequestMapping(value = PostManUrls.GEO_LOC, method = RequestMethod.GET)
	public GeoLocation geoLocation(@RequestParam @ApiParam(defaultValue = "171.50.210.63") String ip)
			throws PostManException, IOException, GeoIp2Exception {
		return geoLocationService.getLocation(ip);
	}

}
