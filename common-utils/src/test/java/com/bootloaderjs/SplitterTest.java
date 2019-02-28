package com.bootloaderjs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitterTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");
	public static final Pattern LINK_CIVIL_ID = Pattern.compile("^LINK (.*)$");
	public static final Pattern LINKD_CIVIL_ID = Pattern.compile("^LINKD <(.*)>$");
	public static final Pattern ENCRYPTED_PROPERTIES = Pattern.compile("^ENC\\((.*)\\)$");

	public static final String SPLITTER_CHAR = ";";
	public static final String KEY_VALUE_SEPARATOR_CHAR = ":";

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		linCivilIdCheck();
	}

	public static void linCivilIdCheck() {
		String testString = "ENC(uTSqb9grs1+vUv3iN8lItC0kl65lMG+8)";
		Matcher x = ENCRYPTED_PROPERTIES.matcher(testString);
		if(x.find()) {
			System.out.println(x.group(1));
		} else {
			System.out.println("No");
		}
		
		
	}

	public static void classNameCheck() {
		String testString = "com.amx.jax.mcq.SampleTask2$$EnhancerBySpringCGLIB$$82858f05";
		System.out.println(testString.split("\\$\\$")[0]);
	}

}
