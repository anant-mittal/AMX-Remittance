package com.amx.jax.cache;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.def.ACtxCacheBox;
import com.amx.jax.def.ICacheBox;

public abstract class CtxCacheBox<T> extends ACtxCacheBox<T> {

	@Autowired(required = false)
	RedissonClient redisson;

	CacheBox<T> cache;

	@Override
	public ICacheBox<T> getCacheBox() {
		if (cache == null) {
			this.cache = new CacheBox<T>("ctxcb-" + this.getClazzName());
			this.cache.setClient(redisson);
		}
		return this.cache;
	}

}
