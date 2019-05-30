package com.amx.jax.cache;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.redisson.api.LocalCachedMapOptions;
import org.redisson.api.LocalCachedMapOptions.EvictionPolicy;
import org.redisson.api.LocalCachedMapOptions.ReconnectionStrategy;
import org.redisson.api.LocalCachedMapOptions.SyncStrategy;
import org.redisson.api.RLocalCachedMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.thavam.util.concurrent.blockingMap.BlockingHashMap;

import com.amx.jax.cache.MCQStatus.MCQStatusCodes;
import com.amx.jax.cache.MCQStatus.MCQStatusError;
import com.amx.jax.def.ICacheBox;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.ArgUtil;
import com.amx.utils.ClazzUtil;

public class CacheBox<T> implements ICacheBox<T> {

	private Logger LOGGER = LoggerService.getLogger(CacheBox.class);

	LocalCachedMapOptions<String, T> localCacheOptions = LocalCachedMapOptions.<String, T>defaults()
			.evictionPolicy(EvictionPolicy.NONE).cacheSize(1000).reconnectionStrategy(ReconnectionStrategy.NONE)
			.syncStrategy(SyncStrategy.INVALIDATE).timeToLive(10000).maxIdle(10000);

	@Autowired(required = false)
	RedissonClient redisson;

	public void setClient(RedissonClient redisson) {
		this.redisson = redisson;
	}

	private RLocalCachedMap<String, T> cache = null;
	private BlockingHashMap<String, T> locker = null;

	public RLocalCachedMap<String, T> map() {
		if (redisson != null) {
			if (locker == null) {
				locker = new BlockingHashMap<String, T>();
			}
			String localCacheName = String.format("%s-%s.%s",
					(ArgUtil.isEmpty(getCahceName()) ? getClazzName() : getCahceName()),
					CacheRedisConfiguration.CODEC_VERSION, version());
			if (cache == null) {
				cache = redisson.getLocalCachedMap(localCacheName,
						localCacheOptions);
			}
			return cache;
		}
		return null;
	}

	String clazzName = null;

	public String getClazzName() {
		if (this.clazzName == null) {
			this.clazzName = ClazzUtil.getClassName(this);
		}
		return clazzName;
	}

	String cahceName = null;

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
		try {
			return this.map().put(key, value);
		} catch (Exception e) {
			LOGGER.error("REDIS_SAVE_EXCEPTION KEY:" + key, e);
			throw new MCQStatusError(MCQStatusCodes.DATA_SAVE_ERROR, "REDIS_SAVE_EXCEPTION KEY:" + key);
		}
	}

	@Override
	public T get(String key) {
		try {
			return this.map().get(key);
		} catch (Exception e) {
			LOGGER.error("REDIS_READ_EXCEPTION KEY:" + key, e);
			// throw new MCQStatusError(MCQStatusCodes.DATA_READ_ERROR,
			// "REDIS_READ_EXCEPTION KEY:" + key);
			return null;
		}
	}

	@Override
	public T putIfAbsent(String key, T value) {
		try {
			return this.map().putIfAbsent(key, value);
		} catch (Exception e) {
			LOGGER.error("REDIS_SAVE_EXCEPTION KEY:" + key, e);
			throw new MCQStatusError(MCQStatusCodes.DATA_SAVE_ERROR, "REDIS_SAVE_EXCEPTION KEY:" + key);
		}
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
		try {
			return this.map().getOrDefault(key, defaultValue);
		} catch (Exception e) {
			return defaultValue;
		}
	}

	public T take(String key, long timeout, TimeUnit unit) throws InterruptedException {
		T item = this.map().get(key);
		long waiting = unit.toSeconds(timeout);
		while (item == null && waiting > 0) {
			item = locker.take(key, 1, unit);
			item = this.map().get(key);
			waiting--;
		}
		return item;
	}

	@Override
	public T getOrDefault(String key) {
		return getOrDefault(key, getDefault());
	}

	@Override
	public T getDefault() {
		return null;
	}

	public Object version() {
		return 0;
	}

}
