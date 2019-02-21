package com.amx.jax.cache;

import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.ATransactionModel;
import com.amx.jax.def.ICacheBox;

/**
 * @deprecated use {@link com.amx.jax.cache.TxCacheBox}
 * @author lalittanwar
 *
 * @param <T>
 */
@Deprecated
public abstract class TransactionModel<T> extends ATransactionModel<T> {

	@Autowired(required = false)
	RedissonClient redisson;

	CacheBox<T> cache;

	@Override
	public ICacheBox<T> getCacheBox() {
		if (cache == null) {
			this.cache = new CacheBox<T>("txm-" + this.getClazzName());
			this.cache.setClient(redisson);
		}
		return this.cache;
	}

	@Override
	public T init() {
		return save(getDefault());
	};

	@Override
	public T commit() {
		return null;
	};

}
