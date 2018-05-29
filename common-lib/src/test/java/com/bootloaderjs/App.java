package com.bootloaderjs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Matcher match = pattern.matcher("${app.prod}");

		if (match.find()) {
			System.out.println("====" + match.group(1));
		}

	}
}
