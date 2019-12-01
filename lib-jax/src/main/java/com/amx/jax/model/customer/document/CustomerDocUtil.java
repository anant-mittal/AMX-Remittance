package com.amx.jax.model.customer.document;

import com.amx.utils.StringUtils;

public class CustomerDocUtil {

	public static String getDescription(String description) {
		String[] words = null;
		if (description.contains("_")) {
			words = description.split("_");
		} else {
			words = description.split("\\s");
		}
		StringBuilder sBuild = new StringBuilder();
		for (String word : words) {
			sBuild.append(StringUtils.capitalize(word));
			sBuild.append(" ");
		}
		return sBuild.toString();
	}
}
