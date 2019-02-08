package com.bootloaderjs;

import java.util.Map;
import java.util.regex.Pattern;

import com.amx.utils.StringUtils;
import com.amx.utils.TimeUtils;

public class SplitterTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	public static final String SPLITTER_CHAR = ";";
	public static final String KEY_VALUE_SEPARATOR_CHAR = ":";

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		String testString = "com.amx.jax.mcq.SampleTask2$$EnhancerBySpringCGLIB$$82858f05";
		System.out.println(testString.split("\\$\\$")[0]);
	}

}
