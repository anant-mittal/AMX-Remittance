package com.amx.utils;

public final class MaskUtil {

	/**
	 * Masks input string as per lenght and maskstring
	 * 
	 * @param input
	 * @param maskLength
	 * @param maskString
	 * @return
	 */
	public static final String maskString(String input, final int maskLength, final String maskString) {

		String regex = ".(?=.{" + maskLength + "})";
		return input.replaceAll(regex, maskString);
	}
}
