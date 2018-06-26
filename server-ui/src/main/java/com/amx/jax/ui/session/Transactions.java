package com.amx.jax.ui.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.ui.response.ResponseWrapper;

/**
 * The Class Transactions.
 */
@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Transactions implements Serializable {

	private static final long serialVersionUID = -7266836144833483130L;

	protected String transactionId = null;

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId
	 *            the new transaction id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Track.
	 */
	public void track() {
		AppContextUtil.setTranxId(this.transactionId);
	}

	/**
	 * Clear.
	 */
	public void clear() {
		this.transactionId = null;
	}

	/**
	 * Start.
	 *
	 * @return the string
	 */
	public String start() {
		String key = AppContextUtil.getTraceId();
		AppContextUtil.setTranxId(key);
		return key;
	}

	/**
	 * Start.
	 *
	 * @param <T>
	 *            the generic type
	 * @param wrapper
	 *            the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> start(ResponseWrapper<T> wrapper) {
		this.transactionId = AppContextUtil.getTranxId();
		return wrapper;
	}

	/**
	 * Track.
	 *
	 * @param <T>
	 *            the generic type
	 * @param wrapper
	 *            the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> track(ResponseWrapper<T> wrapper) {
		this.track();
		return wrapper;
	}

	/**
	 * End.
	 *
	 * @param <T>
	 *            the generic type
	 * @param wrapper
	 *            the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> end(ResponseWrapper<T> wrapper) {
		this.clear();
		return wrapper;
	}

}
