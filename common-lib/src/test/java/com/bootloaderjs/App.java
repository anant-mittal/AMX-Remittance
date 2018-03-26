package com.bootloaderjs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println();
		Matcher matcher = pattern.matcher("com.amx.jax.logger.client.AuditFilter<com.amx.jax.logger.events.UserEvent>");
		if (matcher.find()) {
			System.out.println(matcher.group(1));
		}
	}
}
