/**
 * 
 */
package com.amx.jax.pricer.var;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * @author abhijeet
 *
 */
public final class PricerServiceConstants {

	public static final String TTE = "TTE";
	
	public static final String CHARGES_TYPE = "C";
	
	public static final String SERVICE_PROVIDER_INDICATOR = "SB";
	
	public static final String SETTLEMENT_CURRENCY_CODE = "USD";
	
	public static final String Yes = "Y";
	
	public static final String ACTION_IND_I = "I";
	public static final String ACTION_IND_C = "C";
	public static final String ACTION_IND_T = "T";
	public static final String ACTION_IND_U = "U";
	public static final String ACTION_IND_R = "R";
	public static final String ACTION_IND_P = "P";
	public static final String ACTION_IND_F = "F";
	public static final String RESPONSE_UNKNOWN_ERROR = "UNKNOWN ERROR";
	
	
	public static final String PARAM_BIC_BRANCH = "HSBS";
	
	public static final String PARAM_BENE_ADDRESS = "HSEC";
	
	public static final String PARAM_FEE_DUMMY_ACCOUNT = "HMUS";
	
	public static final BigDecimal BOTH_BANK_SERVICE_COMPONENT = new BigDecimal(777);

	// public static final BigDecimal DEFAULT_ONLINE_SERVICE_ID = BigDecimal.ZERO;

	public static final BigDecimal MAX_BIGD_12 = new BigDecimal(999999999999l);
	
	
	// Def Pricing Scale
	public static int DEF_DECIMAL_SCALE = 9;

	public static MathContext DEF_CONTEXT = new MathContext(DEF_DECIMAL_SCALE, RoundingMode.HALF_EVEN);
	

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
	
	public static enum SERVICE_PROVIDER_BANK_CODE {
		HOME, WU, MONEY;
	}
	
	public static final String SEND_TRNX = "S";
	public static final String REQUEST = "R";
	public static final String RESPONSE = "P";
	
	public static final String FEE_REQUEST = "fee Request";
	public static final String FEE_RESPONSE = "fee Response";
	public static final String COMMIT_REQUEST = "commit Request";
	public static final String COMMIT_RESPONSE = "commit response";
	
	public static final String COUNTRY_AUS_ALPHA3CODE = "AUS";
	public static final String COUNTRY_USA_ALPHA3CODE = "USA";
	
	public static final String Business = "B";
	public static final String Personal = "P";

}
