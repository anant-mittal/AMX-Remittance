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
import com.amx.jax.postman.model.SMS;
import com.amx.jax.postman.service.GeoLocationServiceImpl;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import io.swagger.annotations.ApiParam;

/**
 * The Class GeoServiceController.
 */
@RestController
public class WhatsAppController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(WhatsAppController.class);

	/** The geo location service. */
	@Autowired
	GeoLocationServiceImpl geoLocationService;

	/**
	 * Geo location.
	 *
	 * @param ip
	 *            the ip
	 * @return the geo location
	 * @throws PostManException
	 *             the post man exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws GeoIp2Exception
	 *             the geo ip 2 exception
	 */
	@RequestMapping(value = PostManUrls.WHATS_APP_SEND, method = RequestMethod.POST)
	public GeoLocation geoLocation(@RequestParam SMS sms) throws PostManException, IOException, GeoIp2Exception {
		return null;
	}

}
