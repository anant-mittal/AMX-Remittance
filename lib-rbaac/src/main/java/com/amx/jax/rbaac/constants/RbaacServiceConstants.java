/**
 * 
 */
package com.amx.jax.rbaac.constants;

/**
 * @author abhijeet
 *
 */
public final class RbaacServiceConstants {

	public static enum DEVICE_TYPE {
		MOBILE, PC;
	}

	public static enum ACCESS_KEY {
		YES, NO, VIEW, EDIT, CREATE, READ, UPDATE, DELETE, RESET, VERIFY, ADD, CANCEL;
	}

	public static enum SCOPE {
		GLOBAL, COUNTRY, AREA, BRANCH;
	}

	public static enum PERM_KEY {

		//@formatter:off

		CUSTOMER_INFO("CUSTOMER.INFO"),
		CUSTOMER_ACCOUNT_ONLINE("CUSTOMER.ACCOUNT.ONLINE"),
		CUSTOMER_STATUS("CUSTOMER.STATUS"),
		CUSTOMER_BENEFICIARY("CUSTOMER.BENEFICIARY"),
		CUSTOMER_BENEFICIARY_PAYMENT("CUSTOMER.BENEFICIARY.PAYMENT"),
		CUSTOMER_LOYALTY_POINTS_BALANCE("CUSTOMER.LOYALTY_POINTS.BALANCE");

		// @formatter:on

		private String permKeyString;

		private PERM_KEY(String permKeyString) {
			this.permKeyString = permKeyString;
		}

		public String getPermKeyString() {
			return permKeyString;
		}

		public void setPermKeyString(String permKeyString) {
			this.permKeyString = permKeyString;
		}

	}

}
