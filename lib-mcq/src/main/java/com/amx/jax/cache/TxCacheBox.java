package com.amx.jax.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.ATxCacheBox;
import com.amx.jax.def.ICacheBox;

public abstract class TxCacheBox<T> extends ATxCacheBox<T> {

	@Component
	public class TCache extends CacheBox<T> {
		protected TCache() {
		}
	}

	@Autowired
	TCache cache;

	@Override
	public ICacheBox<T> getCacheBox() {
		return this.cache;
	}

}
