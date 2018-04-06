package com.amx.jax.postman;

import com.amx.jax.postman.model.GeoLocation;

public interface GeoLocationService {

	public GeoLocation getLocation(String ip) throws PostManException;

}
