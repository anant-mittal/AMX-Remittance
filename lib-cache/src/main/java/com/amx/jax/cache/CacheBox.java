package com.amx.jax.cache;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.springframework.beans.factory.annotation.Autowired;

public class CacheBox<T> {

	LocalCachedMapOptions<String, T> localCacheOptions = LocalCachedMapOptions.<String, T>defaults()
			.evictionPolicy(EvictionPolicy.NONE).cacheSize(1000).reconnectionStrategy(ReconnectionStrategy.NONE)
			.syncStrategy(SyncStrategy.INVALIDATE).timeToLive(10000).maxIdle(10000);

	@Autowired
	RedissonClient redisson;

	String cahceName = "common";

	public RLocalCachedMap<String, T> map() {
		return redisson.getLocalCachedMap(cahceName, localCacheOptions);
	}

	protected CacheBox(String name) {

	}

}
