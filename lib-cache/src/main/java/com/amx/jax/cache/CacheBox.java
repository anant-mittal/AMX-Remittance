package com.amx.jax.cache;

import java.util.Map;
import java.util.Set;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.def.ICacheBox;

public class CacheBox<T> implements ICacheBox<T> {

	LocalCachedMapOptions<String, T> localCacheOptions = LocalCachedMapOptions.<String, T>defaults()
			.evictionPolicy(EvictionPolicy.NONE).cacheSize(1000).reconnectionStrategy(ReconnectionStrategy.NONE)
			.syncStrategy(SyncStrategy.INVALIDATE).timeToLive(10000).maxIdle(10000);

	@Autowired(required = false)
	RedissonClient redisson;

	String cahceName = getClass().getName();

	public RLocalCachedMap<String, T> map() {
		if (redisson != null) {
			return redisson.getLocalCachedMap(getCahceName(), localCacheOptions);
		}
		return null;
	}

	public String getCahceName() {
		return cahceName;
	}

	public void setCahceName(String cahceName) {
		this.cahceName = cahceName;
	}

	protected CacheBox(String name) {
		this.cahceName = name;
	}

	protected CacheBox() {

	}

	@Override
	public T put(String key, T value) {
		return this.map().put(key, value);
	}

	@Override
	public T get(String key) {
		return this.map().get(key);
	}

	@Override
	public T putIfAbsent(String key, T value) {
		return this.map().putIfAbsent(key, value);
	}

	@Override
	public T remove(String key) {
		return this.map().remove(key);
	}

	@Override
	public T replace(String key, T value) {
		return this.map().replace(key, value);
	}

	@Override
	public boolean replace(String key, T oldValue, T newValue) {
		return this.map().replace(key, oldValue, newValue);
	}

	@Override
	public boolean remove(String key, Object value) {
		return this.map().remove(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends T> map) {
		this.map().putAll(map);
	}

	@Override
	public Map<String, T> getAll(Set<String> keys) {
		return this.map().getAll(keys);
	}

	@Override
	public long fastRemove(String... keys) {
		return this.map().fastRemove(keys);
	}

	@Override
	public boolean fastPut(String key, T value) {
		return this.map().fastPut(key, value);
	}

	@Override
	public boolean fastPutIfAbsent(String key, T value) {
		return this.map().fastPutIfAbsent(key, value);
	}

	@Override
	public T getOrDefault(String key, T defaultValue) {
		return this.map().getOrDefault(key, defaultValue);
	}

}
