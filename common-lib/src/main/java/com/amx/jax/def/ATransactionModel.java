package com.amx.jax.def;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;
import com.amx.utils.Utils;

public abstract class ATransactionModel<T> {

	public class CacheBoxWrapper {
		T model = null;

		public T getModel() {
			return model;
		}

		public void setModel(T model) {
			this.model = model;
		}

		public CacheBoxWrapper(T model) {
			super();
			this.model = model;
		}

	}

	public abstract ICacheBox<T> getCacheBox();

	protected String getTranxId() {
		String key = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		if (Utils.isStringEmpty(key)) {
			key = ArgUtil.parseAsString(ContextUtil.map().get(ContextUtil.TRACE_ID));
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, key);
		}
		return key;
	}

	public void save(T model) {
		getCacheBox().put(getTranxId(), model);
	}

	public T get() {
		return this.get(null);
	}

	public T get(T defaultValue) {
		return getCacheBox().getOrDefault(getTranxId(), defaultValue);
	}

	public abstract T init();

	public abstract T commit();
}
