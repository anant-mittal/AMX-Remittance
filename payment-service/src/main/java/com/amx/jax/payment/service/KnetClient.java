package com.amx.jax.payment.service;

import org.springframework.stereotype.Component;

import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class KnetClient implements PayGClient {

	@Override
	public Services getClientCode() {
		return Services.KNET;
	}

	@Override
	public void initialize(PayGParams payGParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public void capture(PayGResponse payGResponse) {
		// TODO Auto-generated method stub
	}

}
