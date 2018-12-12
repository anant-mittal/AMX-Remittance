package com.amx.jax.offsite.device.signpad;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

@Component
public class SignPadBox extends CacheBox<SignPadData> {

	@Override
	public SignPadData getDefault() {
		return new SignPadData();
	}

}