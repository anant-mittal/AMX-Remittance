package com.bootloaderjs;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class DateTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		Date sysDate = new Date(System.currentTimeMillis());
		System.out.println("=====" + sysDate.toString());
	}

}
