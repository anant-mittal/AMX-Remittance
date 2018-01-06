package com.amx.jax.payment.gateway;

public interface PayGClient {

	enum Client {
		KNET("knet"), BAHKNET("bahknet"), OMANNET("OmanNet");

		private String pgEnum;

		Client(String pgEnum) {
			this.pgEnum = pgEnum;
		}

		public String getPgEnum() {
			return this.pgEnum;
		}
	}

	void initialize();

	void capture();

	public String getClientName();

}
