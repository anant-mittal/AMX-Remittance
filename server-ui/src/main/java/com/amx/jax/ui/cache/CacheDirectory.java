package com.amx.jax.ui.cache;

import org.springframework.stereotype.Component;

import com.amx.jax.cache.CacheBox;

public class CacheDirectory {

	public static final String NAME_PROEFIX = "name_prefix";

	@Component
	public static class Prefix extends CacheBox<String> {

		public Prefix() {
			super(NAME_PROEFIX);
		}

	}

}
