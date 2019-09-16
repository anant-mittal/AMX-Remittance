package com.amx.jax.cache;

import org.springframework.stereotype.Component;

@Component
public class SampleTestCacheBox extends CacheBox<String> {

	@Override
	public String getDefault() {
		return "DEFAULT";
	}

}