package com.bootloaderjs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.SimpleTimeZone;
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
		Date sysDate = new Date(0L);
		System.out.println("=====" + sysDate.toString());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YYYY HH:mm:ss");
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		String oldString = sdf.format(sysDate);
		System.out.println("=====" + oldString);
		sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		System.out.println("=====" + sdf.parse(oldString).toString());
	}

}
