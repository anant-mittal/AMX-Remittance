package com.amx.jax.ui.session;

import java.io.Serializable;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.scheduling.config.IntervalTask;
import org.springframework.stereotype.Component;

import com.amx.jax.AppContextUtil;
import com.amx.jax.http.CommonHttpRequest;
import com.amx.jax.model.AuthState.AuthFlow;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil.HashBuilder;

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
	 * @param transactionId the new transaction id
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
	 * @param         <T> the generic type
	 * @param wrapper the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> start(ResponseWrapper<T> wrapper) {
		this.transactionId = AppContextUtil.getTranxId();
		return wrapper;
	}

	/**
	 * Track.
	 *
	 * @param         <T> the generic type
	 * @param wrapper the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> track(ResponseWrapper<T> wrapper) {
		this.track();
		return wrapper;
	}

	/**
	 * End.
	 *
	 * @param         <T> the generic type
	 * @param wrapper the wrapper
	 * @return the response wrapper
	 */
	public <T> ResponseWrapper<T> end(ResponseWrapper<T> wrapper) {
		this.clear();
		return wrapper;
	}

	private String flowToken = null;

	@Autowired
	private CommonHttpRequest commonHttpRequest;

	public void create(AuthFlow authFlow) {
		this.flowToken = new HashBuilder().secret(authFlow.toString()).message(AppContextUtil.getTraceId())
				.interval(120).toHMAC().output();
		commonHttpRequest.setCookie(this.flowToken, this.flowToken, 900);
	}

	public boolean validate(AuthFlow authFlow) {
		Cookie kooky = commonHttpRequest.getCookie(this.flowToken);
		if (!ArgUtil.isEmpty(kooky)) {
			return kooky.getValue().equals(this.flowToken);
		}
		return false;
	}

}
