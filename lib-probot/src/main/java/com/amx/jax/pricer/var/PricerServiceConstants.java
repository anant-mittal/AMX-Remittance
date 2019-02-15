/**
 * 
 */
package com.amx.jax.pricer.var;

/**
 * @author abhijeet
 *
 */
public final class PricerServiceConstants {

	public static String TTE = "TTE";

	private PricerServiceConstants() {
		// Not Allowed
		super();
	}

	// public static enum DEVICE_TYPE {
	// MOBILE, PC;
	// }

	public static enum PRICE_BY {
		ROUTING_BANK, COUNTRY;
	}

	public static enum DISCOUNT_TYPE {
		CHANNEL, CUSTOMER_CATEGORY, AMOUNT_SLAB;
	}

	public static enum CUSTOMER_CATEGORY {
		BRONZE, SILVER, GOLD, PLATINUM;
	}

}
