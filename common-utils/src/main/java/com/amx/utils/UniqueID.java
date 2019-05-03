package com.amx.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

//TODO: Auto-generated Javadoc
/**
 * The Class UniqueID.
 */
public final class UniqueID {

	/** The Constant INT_22. */
	private static final int INT_22 = 22;

	/** The Constant PREF. */
	public static final String PREF = Random.randomAlpha(3);
	public static final Pattern SYSTEM_STRING_PATTERN = Pattern.compile("^([A-Z]{3})-([\\w]+)-([\\w]+)-(\\w+)$");

	/** The atom. */
	private static AtomicInteger atom = new AtomicInteger();

	/**
	 * Instantiates a new unique id.
	 */
	private UniqueID() {
		throw new IllegalStateException("This is a class with static methods and should not be instantiated");
	}

	/**
	 * Generate.
	 *
	 * @return the long
	 */
	public static long generate() {
		return ((System.currentTimeMillis() << INT_22) | (((1 << INT_22) - 1) & atom.getAndIncrement()));
	}

	/**
	 * Generate string.
	 *
	 * @return : Unique String ID
	 */
	public static String generateString() {
		return Long.toString(generate(), 36);
	}

	/**
	 * Unique Session String across Systems
	 * 
	 * @return
	 */
	public static String generateSessionId() {
		return PREF + "-" + generateString();
	}

	public static String generateRequestId(String sessionId, String requestPrefix) {
		return sessionId + "-" + requestPrefix + "-" + Long.toString(generate(), 36);
	}

	/**
	 * Generate string.
	 *
	 * @param midfix the midfix
	 * @return : Unique String ID
	 */
	public static String generateSystemString(String midfix, String prefix) {
		return PREF + "-" + midfix + "-" + prefix + "-" + Long.toString(generate(), 36);
	}

}