package com.bootloaderjs;

import java.util.Calendar;
import java.util.regex.Pattern;

import com.amx.utils.TimeUtils;

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

		Calendar calendar = Calendar.getInstance();
		System.out.println("** " + calendar.get(Calendar.HOUR_OF_DAY) + "   " + calendar.get(Calendar.MINUTE));
		System.out.println(
				"==" + TimeUtils.inHourSlot(4, 1) + "==" + TimeUtils.inHourSlot(4, 2) + "   "
						+ TimeUtils.inHourSlot(4, 3));
	}

	private static long rotateTime(long millis, int i) {
		return (System.currentTimeMillis() / (millis)) & i;
	}

	private static long rotateTimeReverse(long millis, int i) {
		return i - (System.currentTimeMillis() / (millis)) & i;
	}

}
