package com.amx.jax.payment.gateway;

import org.springframework.ui.Model;

import com.amx.jax.payment.PayGServiceCode;

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
	String capture(Model model);

}