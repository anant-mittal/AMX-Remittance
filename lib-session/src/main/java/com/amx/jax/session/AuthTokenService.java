package com.amx.jax.session;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;

public class AuthTokenService<T> {

	LocalCachedMapOptions<String, String> localCacheOptions = LocalCachedMapOptions.<String, String>defaults()
			.evictionPolicy(EvictionPolicy.NONE).cacheSize(1000).reconnectionStrategy(ReconnectionStrategy.NONE)
			.syncStrategy(SyncStrategy.INVALIDATE).timeToLive(10000).maxIdle(10000);
	
}
