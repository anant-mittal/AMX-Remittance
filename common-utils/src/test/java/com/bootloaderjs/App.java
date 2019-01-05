package com.bootloaderjs;

import java.util.regex.Pattern;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long timeout = 100L;
		long minute = System.currentTimeMillis() / (1000 * 60);
		System.out.println(
				"==" + minute + "===" + rotateTime(1000 * 1, 0x3));
	}

	private static long rotateTime(long millis, int i) {
		return (System.currentTimeMillis() / (millis)) & i;
	}

	private static long rotateTimeReverse(long millis, int i) {
		return i - (System.currentTimeMillis() / (millis)) & i;
	}

}
