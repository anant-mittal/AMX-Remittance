package com.amx.jax.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.ATransactionModel;
import com.amx.jax.def.ICacheBox;

public abstract class TransactionModel<T> extends ATransactionModel<T> {

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

	@Override
	public T init() {
		return save(getDefault());
	};

	@Override
	public T commit() {
		return null;
	};
}
