package com.amx.jax.device;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

/**
 * NotipyData is saved against employeId
 * 
 * @author lalittanwar
 *
 */
@Component
public class NotipyBox extends CacheBox<NotipyData> {

	@Override
	public NotipyData getDefault() {
		return new NotipyData();
	}

	public Object version() {
		return 1;
	}
}