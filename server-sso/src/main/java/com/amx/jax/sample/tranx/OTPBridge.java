package com.amx.jax.sample.tranx;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class OTPBridge extends TransactionBridge<OTPAware> {

	public OTPBridge(List<OTPAware> beans) {
		super(beans);
	}

	public void sendOTP() {

	}

}
