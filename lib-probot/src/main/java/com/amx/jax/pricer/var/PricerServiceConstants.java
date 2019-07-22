/**
 * 
 */
package com.amx.jax.pricer.var;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author abhijeet
 *
 */
public final class PricerServiceConstants {

	public static final String TTE = "TTE";

	// public static final BigDecimal DEFAULT_ONLINE_SERVICE_ID = BigDecimal.ZERO;

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

	public static enum SERVICE_INDICATOR {

		EFT(101, "EFT"), TT(102, "TT"), CASH(103, "CASH"), DD(104, "DD"), TT_OTHER(105, "TT OTHER");

		private int serviceId;
		private String description;

		private static final Map<Integer, SERVICE_INDICATOR> BY_ID = new HashMap<Integer, SERVICE_INDICATOR>();

		static {
			for (SERVICE_INDICATOR ind : values()) {
				BY_ID.put(ind.serviceId, ind);
			}
		}

		private SERVICE_INDICATOR(int serviceId, String description) {
			this.serviceId = serviceId;
			this.description = description;
		}

		public static SERVICE_INDICATOR getByServiceId(int serviceId) {
			return BY_ID.get(serviceId);
		}

		public int getServiceId() {
			return serviceId;
		}

		public void setServiceId(int serviceId) {
			this.serviceId = serviceId;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

}
