package com.amx.utils;


/**
 * The Class TimeUtils.
 */
public class TimeUtils {
	
	/**
	 * Time since.
	 *
	 * @param timethen the timethen
	 * @return the long
	 */
	public static long timeSince(long timethen) {
		return System.currentTimeMillis() - timethen;
	}
}
