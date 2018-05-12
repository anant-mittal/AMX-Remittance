package com.amx.jax.ui.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConstants;
import com.amx.jax.AppContextUtil;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class Transactions implements Serializable {

	private static final long serialVersionUID = -7266836144833483130L;

	protected String transactionId = null;

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void track() {
		ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, this.transactionId);
	}

	public void clear() {
		this.transactionId = null;
	}

	public String start() {
		String key = AppContextUtil.getTraceId();
		AppContextUtil.setTranxId(key);
		return key;
	}

	public <T> ResponseWrapper<T> start(ResponseWrapper<T> wrapper) {
		this.transactionId = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		return wrapper;
	}

	public <T> ResponseWrapper<T> track(ResponseWrapper<T> wrapper) {
		this.track();
		return wrapper;
	}

	public <T> ResponseWrapper<T> end(ResponseWrapper<T> wrapper) {
		this.clear();
		return wrapper;
	}

}
