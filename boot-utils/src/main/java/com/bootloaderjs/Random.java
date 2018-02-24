package com.bootloaderjs;

public class Random {

	private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final String ALPHA_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String NUMERIC_STRING = "0123456789";

	public static int getInt(int min, int max) {
		return (int) (Math.random() * (max - min + 1) + min);
	}

	public static String randomAlphaNumeric(int count) {
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
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
		StringBuilder builder = new StringBuilder();
		while (count-- != 0) {
			int character = (int) (Math.random() * NUMERIC_STRING.length());
			builder.append(NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}
