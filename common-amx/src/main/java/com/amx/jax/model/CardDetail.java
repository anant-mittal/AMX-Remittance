package com.amx.jax.model;

import java.util.HashMap;
import java.util.Map;

public class CardDetail {
	String identity;

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	Map<String, Object> cardata = new HashMap<String, Object>();

	public Map<String, Object> getCardata() {
		return cardata;
	}

	public void setCardata(Map<String, Object> cardata) {
		this.cardata = cardata;
	}

}
