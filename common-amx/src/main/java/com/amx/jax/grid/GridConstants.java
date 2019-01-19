package com.amx.jax.grid;

import java.text.SimpleDateFormat;
import java.util.SimpleTimeZone;

public class GridConstants {

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

	public static final String GRID_DATE_FORMAT_SQL = "DD-MM-YYYY";
	public static final String GRID_DATE_FORMAT_JAVA = "dd-MM-YYYY";
	public static final String GRID_TIME_FORMAT_SQL = "DD-MM-YYYY HH24:MI:SS";
	public static final String GRID_TIME_FORMAT_JAVA = "dd-MM-YYYY HH:mm:ss";
	public static final SimpleDateFormat GRID_TIME_FORMATTER_JAVA = new SimpleDateFormat(GRID_TIME_FORMAT_JAVA);
	public static final SimpleDateFormat GRID_TIME_FORMATTER_JAVA_UTC = new SimpleDateFormat(GRID_TIME_FORMAT_JAVA);

	static {
		GRID_TIME_FORMATTER_JAVA_UTC.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
	}

}