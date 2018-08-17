package com.amx.jax.postman.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.postman.GeoLocationService;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManUrls;
import com.amx.jax.postman.model.GeoLocation;
import com.amx.jax.rest.RestService;

@Component
public class GeoLocationClient implements GeoLocationService {

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	public GeoLocation getLocation(String ip) throws PostManException {
		try {
			return restService.ajax(appConfig.getPostmapURL()).path(PostManUrls.GEO_LOC).queryParam("ip", ip).get()
					.as(GeoLocation.class);
		} catch (Exception e) {
			throw new PostManException(e);
		}
	};

}
