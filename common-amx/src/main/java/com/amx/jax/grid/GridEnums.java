package com.amx.jax.grid;

public class GridEnums {

	public static enum FilterOperater {
		GT(">"), GTE(">="), EQ("="), STE("<="), ST("<"), LK("like");

		String sign;

		FilterOperater(String sign) {
			this.sign = sign;
		}

		public String getSign() {
			return sign;
		}
	}

	public static enum FilterDataType {
		STRING, NUMBER, DATE, TIME, TIMESTAMP
	}
}