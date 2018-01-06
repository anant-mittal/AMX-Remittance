package com.amx.jax.payment.service;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.amx.jax.payment.gateway.PayGParams;

@Component
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PayGSession {

	PayGParams payGParams = null;

	public PayGParams getPayGParams() {
		return payGParams;
	}

	public void setPayGParams(PayGParams payGParams) {
		this.payGParams = payGParams;
	}
}
