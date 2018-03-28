package com.amx.jax.cache;

import com.amx.jax.def.ATransactionModel;
import com.amx.jax.def.ICacheBox;

public abstract class TransactionModel<T> extends ATransactionModel<T> {

	public class TCache extends CacheBox<T> {
		protected TCache() {
		}
	}

	TCache cache = new TCache();

	@Override
	public ICacheBox<T> getCacheBox() {
		return this.cache;
	}

	public abstract T init();

	public abstract T commit();
}
