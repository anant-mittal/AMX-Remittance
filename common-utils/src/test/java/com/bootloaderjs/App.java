package com.bootloaderjs;

import java.util.regex.Pattern;

import com.amx.utils.StringUtils;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 1), "defg");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 1), "xabdefg");

		printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 1), "abde");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 1), "abdefgx");

		printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 0), "xabdefg");

		printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 0), "abdefgx");

	}

	public static void printAndCheck(String str, String check) {
		System.out.println(String.format("%15s === %15s %15s", str.equals(check), check, str));
	}

	private static long rotateTime(long millis, int i) {
		return (System.currentTimeMillis() / (millis)) & i;
	}

	private static long rotateTimeReverse(long millis, int i) {
		return i - (System.currentTimeMillis() / (millis)) & i;
	}

}
