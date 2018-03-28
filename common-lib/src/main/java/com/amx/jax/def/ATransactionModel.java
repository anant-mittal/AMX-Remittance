package com.amx.jax.def;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.Utils;

public abstract class ATransactionModel<T> {

	public abstract ICacheBox<T> getCacheBox();

	public void save(T model) {
		String key = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		if (Utils.isStringEmpty(key)) {
			getCacheBox().put(key, model);
		}
	}

	public T get() {
		return this.get(null);
	}

	public T get(T defaultValue) {
		String key = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		if (Utils.isStringEmpty(key)) {
			return getCacheBox().getOrDefault(key, defaultValue);
		}
		return defaultValue;
	}

	public abstract T init();

	public abstract T commit();
}
