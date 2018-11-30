package com.amx.jax.payment.gateway;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.payg.PayGParams;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayGSession {

	String callback = null;

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	PayGParams payGParams = null;

	public PayGParams getPayGParams() {
		return payGParams;
	}

	public void setPayGParams(PayGParams payGParams) {
		this.payGParams = payGParams;
	}
}
