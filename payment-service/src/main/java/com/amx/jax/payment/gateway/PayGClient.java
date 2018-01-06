package com.amx.jax.payment.gateway;

public interface PayGClient {

	enum Services {
		KNET("knet"), BAHKNET("bahknet"), OMANNET("OmanNet"),

		/**
		 * DOnt use this one.
		 */
		DEFAULT("default");

		private String pgEnum;

		Services(String pgEnum) {
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
	public Services getClientCode();

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
	void capture(PayGResponse payGResponse);

}
