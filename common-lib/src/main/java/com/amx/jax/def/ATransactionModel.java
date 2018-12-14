package com.amx.jax.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amx.jax.AppContextUtil;
import com.amx.utils.ArgUtil;

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
		String key = AppContextUtil.getTranxId();
		if (ArgUtil.isEmptyString(key)) {
			key = AppContextUtil.getTraceId();
			AppContextUtil.setTranxId(key);
			LOGGER.info("************ Creating New Tranx Id {} *******************", key);
		} else {
			LOGGER.debug("************ Exisitng Tranx Id {} *******************", key);
		}
		return key;
	}

	public T save(T model) {
		getCacheBox().put(getTranxId(), model);
		return model;
	}

	public T save() {
		T model = this.get(null);
		if (model == null) {
			return null;
		}
		this.save(model);
		return model;
	}

	public void clear(String tranxId) {
		getCacheBox().remove(tranxId);
	}

	public T get() {
		T model = this.get(null);
		if (model == null) {
			return this.get(getDefault());
		}
		return model;
	}

	public T get(T defaultValue) {
		return getCacheBox().getOrDefault(getTranxId(), defaultValue);
	}

	public abstract T init();

	public abstract T commit();

	public T getDefault() {
		return null;
	}

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
