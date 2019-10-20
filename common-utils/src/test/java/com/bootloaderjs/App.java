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
		String pad = "FffffffffffffffffffffffffffffffG";
		String src = "SssssssssssssT";
		System.out.println(pad);
		System.out.println(src);
		System.out.println(StringUtils.pad(src, pad, 1, 0));
		System.out.println(StringUtils.pad(src, pad, 1, 1));

		System.out.println("Set 1");
		printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 0, 0),
				"SssssssssssssT");
		printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 0, 1),
				"SssssssssssssTfffffffffffffffffG");
		printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 1, 0),
				"SssssssssssssT");
		printAndCheck(StringUtils.pad("SssssssssssssT", "FffffffffffffffffffffffffffffffG", 1, 1),
				"FfffffffffffffffffSssssssssssssT");

		System.out.println("Set 2");
		printAndCheck(StringUtils.pad("SssssssssssssT", "", 0, 0), "SssssssssssssT");
		printAndCheck(StringUtils.pad("SssssssssssssT", "", 0, 1), "");
		printAndCheck(StringUtils.pad("SssssssssssssT", "", 1, 0), "SssssssssssssT");
		printAndCheck(StringUtils.pad("SssssssssssssT", "", 1, 1), "");

		System.out.println("Set 3");
		printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxx", 0, 1), "abde");
		printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxx", 1, 1), "defg");

		System.out.println("Set 4");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 0, 1), "abdefgx");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 0), "abdefg");
		printAndCheck(StringUtils.pad("abdefg", "xxxxxxx", 1, 1), "xabdefg");

		System.out.println("Set 5");
		printAndCheck(StringUtils.pad("", "xxxxxxx", 0, 0), "");
		printAndCheck(StringUtils.pad("", "xxxxxxx", 0, 1), "xxxxxxx");
		printAndCheck(StringUtils.pad("", "xxxxxxx", 1, 0), "");
		printAndCheck(StringUtils.pad("", "xxxxxxx", 1, 1), "xxxxxxx");

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
