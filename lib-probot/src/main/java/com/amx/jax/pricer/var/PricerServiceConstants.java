/**
 * 
 */
package com.amx.jax.pricer.var;

import java.math.BigDecimal;

/**
 * @author abhijeet
 *
 */
public final class PricerServiceConstants {

	public static final String TTE = "TTE";
	
	public static final String CHARGES_TYPE = "C";
	
	public static final String SETTLEMENT_CURRENCY_CODE = "USD";
	
	public static final String Yes = "Y";
	
	public static final String ACTION_IND_I = "I";
	
	public static final BigDecimal BOTH_BANK_SERVICE_COMPONENT = new BigDecimal(777);

	//public static final BigDecimal DEFAULT_ONLINE_SERVICE_ID = BigDecimal.ZERO;

	public static final BigDecimal MAX_BIGD_12 = new BigDecimal(999999999999l);

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

	public static enum SERVICE_GROUP {

		CASH("C"), BANK("B");

		private String groupCode;

		private SERVICE_GROUP(String groupCode) {
			this.groupCode = groupCode;
		}

		public String getGroupCode() {
			return this.groupCode;
		}

	}

	public static enum PRICE_TYPE {
		BENE_DEDUCT, NO_BENE_DEDUCT;
	}

}
