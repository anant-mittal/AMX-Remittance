package com.amx.jax.payment.gateway;

import org.springframework.ui.Model;

public interface PayGClient {

	enum ServiceCode {
		KNET("knet"), BAHKNET("bahknet"), OMANNET("OmanNet"),

		/**
		 * DOnt use this one.
		 */
		DEFAULT("default");

		private String pgEnum;

		ServiceCode(String pgEnum) {
			this.pgEnum = pgEnum;
		}

		public String getPgEnum() {
			return this.pgEnum;
		}
	}

	/**
	 * This should return the Client Code identifier
	 * 
	 * @return
	 */
	public ServiceCode getClientCode();

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
