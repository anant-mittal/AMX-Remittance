package com.amx.utils;

import java.util.Iterator;
import java.util.List;

public class Utils {

	public static String COMMA = ",";

	public static String concatenate(List<String> listOfItems, String separator) {
		if (ArgUtil.isEmpty(listOfItems)) {
			return Constants.BLANK;
		}
		StringBuilder sb = new StringBuilder();
		Iterator<String> stit = listOfItems.iterator();

		while (stit.hasNext()) {
			sb.append(stit.next());
			if (stit.hasNext()) {
				sb.append(separator);
			}
		}
		return sb.toString();
	}

	public static String commaConcat(List<String> listOfItems) {
		return concatenate(listOfItems, COMMA);
	}

}
