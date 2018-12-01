package com.amx.jax.payment.gateway;

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
	 * @param respModel
	 */
	void initialize(PayGParams params, PaymentGateWayResponse gatewayResponse);

	/**
	 * 
	 * @param payGResponse
	 */
	public PaymentGateWayResponse capture(PayGParams params, PaymentGateWayResponse gatewayResponse);

}
