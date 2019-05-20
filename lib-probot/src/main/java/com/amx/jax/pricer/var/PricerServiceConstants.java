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

	public static String BIG_Y = "Y";

	public static String BIG_YES = "YES";

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

		CHANNEL("CHANNEL"), CUSTOMER_CATEGORY("CSTMRCAT"), AMOUNT_SLAB("PIPS");

		private String typeKey;

		DISCOUNT_TYPE(String typeKey) {
			this.typeKey = typeKey;
		}

		public String getTypeKey() {
			return typeKey;
		}

		public void setTypeKey(String typeKey) {
			this.typeKey = typeKey;
		}

	}

	public static enum CUSTOMER_CATEGORY {
		BRONZE, SILVER, GOLD, PLATINUM;
	}

}
