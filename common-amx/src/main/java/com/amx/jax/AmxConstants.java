package com.amx.jax;

public final class AmxConstants {

	public static class AmxValidator {
		public static final String OTP = "^[0-9]{6}$";
	}

	public static final String SHH_DONT_TELL_ANYONE = "SHH..DONT.TELL.ANYONE";

	@Deprecated
	public static final long OTP_TTL = AmxConstants.OFFLINE_OTP_TTL;

	/**
	 * Offline Otp TTL in seconds
	 */
	public static final long OFFLINE_OTP_TTL = 60;

	/**
	 * Offline Otp tolerance in Seconds
	 */
	public static final long OFFLINE_OTP_TOLERANCE = 60;

	/**
	 * SMS Otp TTL in Seconds
	 */
	public static final long SMS_OTP_TTL = 15 * 60;

	public static final int OTP_LENGTH = 6;
	

}
