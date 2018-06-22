package com.amx.utils;

public class Random {

	private Random() {
		// private constructor to hide the implicit public one.
	}

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMERIC_STRING = "0123456789";

	public static int getInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	public static String randomAlpha(int count, String alphaString) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * alphaString.length());
			builder.append(alphaString.charAt(character));
		}
		return builder.toString();
	}

	public static String randomAlpha(int count) {
		return randomAlpha(count, ALPHA_STRING);
	}

	public static String randomNumeric(int count) {
		return randomAlpha(count, NUMERIC_STRING);
	}

	public static String randomAlphaNumeric(int count) {
		return randomAlpha(count, ALPHA_NUMERIC_STRING);
	}
}
