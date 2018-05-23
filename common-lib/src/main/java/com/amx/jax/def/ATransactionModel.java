package com.amx.jax.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.AppConstants;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

public abstract class ATransactionModel<T> {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

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
		if (ArgUtil.isEmptyString(key)) {
			key = ArgUtil.parseAsString(ContextUtil.map().get(ContextUtil.TRACE_ID));
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, key);
			LOGGER.info("************ Creating New Tranx Id {} *******************", key);
		} else {
			LOGGER.info("************ Exisitng Tranx Id {} *******************", key);
		}
		return key;
	}

	public T save(T model) {
		getCacheBox().put(getTranxId(), model);
		return model;
	}

	public T get() {
		return this.get(null);
	}

	public T get(T defaultValue) {
		return getCacheBox().getOrDefault(getTranxId(), defaultValue);
	}

	public abstract T init();

	public abstract T commit();

	/**
	 * Checks if transaction model is present if not then create new by calling init
	 * and return model
	 */
	public T getWithInit() {
		T existingModel = this.get();
		if (existingModel == null) {
			init();
		}
		return this.get();
	}
}
