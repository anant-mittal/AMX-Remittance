package com.bootloaderjs;

import java.util.Map;
import java.util.regex.Pattern;

import com.amx.utils.StringUtils;
import com.amx.utils.TimeUtils;

public class SplitterTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	public static final String SPLITTER_CHAR = ";";
	public static final String KEY_VALUE_SEPARATOR_CHAR = ":";

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String testString = "EMAIL:dssds;MOBILE:sdsdsdsdsds;CUST_NAME:dsdsdsdsds;TRNXAMT:wewewewewe;"
				+ "LOYALTY:wedrererr;TRNREF:wewewewewe;TRNDATE:wewewewew;LANG_ID:wxfgtrtrtrt;TNT:rtrtrtr";

		int num = 1000000;
		long timeout = System.currentTimeMillis();
		for (int i = 0; i < num; i++) {
			Map<String, String> event_data_map = StringUtils.getMapFromString(
					SPLITTER_CHAR,
					KEY_VALUE_SEPARATOR_CHAR, testString);
		}
		System.out.println("Guava" + TimeUtils.timeSince(timeout));
		timeout = System.currentTimeMillis();
//		for (int i = 0; i < num; i++) {
//			Map<String, String> event_data_map = StringUtils.getMapFromStringCommon(
//					SPLITTER_CHAR,
//					KEY_VALUE_SEPARATOR_CHAR, testString);
//		}
		System.out.println("Norm" + TimeUtils.timeSince(timeout));
	}

}
