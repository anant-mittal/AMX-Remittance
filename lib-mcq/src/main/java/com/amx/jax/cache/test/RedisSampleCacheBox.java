package com.amx.jax.cache.test;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

@Component
public class RedisSampleCacheBox extends CacheBox<String> {

	@Override
	public String getDefault() {
		return "DEFAULT";
	}

}