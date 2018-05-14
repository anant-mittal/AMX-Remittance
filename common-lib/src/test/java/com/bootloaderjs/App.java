package com.bootloaderjs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^([A-Z]{3})-([\\w]+)-(\\w+)$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("====");

		Matcher matcher = pattern.matcher("HKK-1cmqzbzurupkw-1cmqzc4npbnd7");
		if (matcher.find()) {
			System.out.println(matcher.group(2));
		}
	}
}
