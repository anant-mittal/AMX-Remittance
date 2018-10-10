package com.amx.jax.postman.api;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.event.TriggerEvent;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.tunnel.ITunnelService;
import com.maxmind.geoip2.exception.GeoIp2Exception;

/**
 * The Class GeoServiceController.
 */
@RestController
public class EventController {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);

	private static ITunnelService tunnelService;

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
	@RequestMapping(value = PostManUrls.EVENT_PUBLISH, method = RequestMethod.GET)
	public long publishEvent(@RequestParam String event, @PathVariable String id,
			@RequestParam(required = false) String value) throws PostManException, IOException {
		return tunnelService.shout(event, new TriggerEvent(event, id, value));
	}

}
