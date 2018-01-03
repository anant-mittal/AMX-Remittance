/**
 * 
 */
package com.amx.jax.payment.constant;

/**
 * @author Viki
 *
 */
public enum PGEnum {
	
	KNET("knet"),
	BAHKNET("bahknet"), 
	OMANNET("OmanNet");

	private String pgEnum;

	PGEnum(String pgEnum) {
		this.pgEnum = pgEnum;
	}

	/**
	 * @return the discountType
	 */
	public String getPgEnum() {
		return this.pgEnum;
	}

}
