package com.amx.utils;

/**
 * The Class TimeUtils.
 */
public class TimeUtils {

	/**
	 * Time since.
	 *
	 * @param timethen
	 *            the timethen
	 * @return the long
	 */
	public static long timeSince(long timethen) {
		return System.currentTimeMillis() - timethen;
	}

	/**
	 * Like expired, timeThen you pass has expired, based on maxAge provided. If
	 * maxAge is 0 then, true means its PAST-TimeStampe and false means its FUTRUE
	 * TimeStamp.
	 * 
	 * @param timeThen
	 *            - timeStamp we want to check or creation time of timestamp, or
	 *            birth-timestamp of entity
	 * @param maxAge
	 *            - maximum age of timestamp or entity
	 * @return
	 */
	public static boolean isDead(long timeThen, long maxAge) {
		return (System.currentTimeMillis() - timeThen) > maxAge;
	}
}
