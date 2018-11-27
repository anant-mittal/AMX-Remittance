/**
 * 
 */
package com.amx.jax.rbaac.constants;

/**
 * @author abhijeet
 *
 */
public final class RbaacServiceConstants {

	private RbaacServiceConstants() {
		// Not Allowed
		super();
	}

//	public static enum DEVICE_TYPE {
//		MOBILE, PC;
//	}

	public static enum ACCESS_KEY {
		YES, NO, VIEW, EDIT, CREATE, READ, UPDATE, DELETE, RESET, VERIFY, ADD, CANCEL, SEND;
	}

	public static enum SCOPE {
		GLOBAL(1), COUNTRY(2), AREA(3), BRANCH(4);

		// Custom Ordinal
		private int scopeIndex;

		private SCOPE(int scopeIndex) {
			this.scopeIndex = scopeIndex;
		}

		public int getScopeIndex() {
			return scopeIndex;
		}

		public boolean isWithinMyScope(SCOPE toScope) {
			if (this.scopeIndex <= toScope.getScopeIndex()) {
				return true;
			}

			return false;
		}

	}

	public static enum PERM_KEY {

		//@formatter:off

		CUSTOMER_INFO("CUSTOMER.INFO"),
		CUSTOMER_ACCOUNT_ONLINE("CUSTOMER.ACCOUNT.ONLINE"),
		CUSTOMER_STATUS("CUSTOMER.STATUS"),
		CUSTOMER_BENEFICIARY("CUSTOMER.BENEFICIARY"),
		CUSTOMER_BENEFICIARY_PAYMENT("CUSTOMER.BENEFICIARY.PAYMENT"),
		CUSTOMER_LOYALTY_POINTS_BALANCE("CUSTOMER.LOYALTY_POINTS.BALANCE"),
		MRKT_MGMT_PUSH_NOTIFICATION("MRKT_MGMT.PUSH_NOTIFICATION"),
		CUSTOMER_MGMT_REMITTANCE("CUSTOMER_MGMT.REMITTANCE");

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
