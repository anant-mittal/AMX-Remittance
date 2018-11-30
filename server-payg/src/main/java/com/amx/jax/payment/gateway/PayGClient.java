package com.amx.jax.payment.gateway;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.payg.PayGParams;

public interface PayGClient {

	/**
	 * This should return the Client Code identifier
	 * 
	 * @return
	 */
	public PayGServiceCode getClientCode();

	/**
	 * To initiate PaymentGateway Client for payment, this method is called before
	 * payment actually starts
	 * 
	 * @param payGParams
	 */
	void initialize(PayGParams payGParams);

	/**
	 * 
	 * @param payGResponse
	 */
	PayGResponse capture(PayGResponse payGResponse, Channel channel, Object product);

}
