package com.amx.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CryptoUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

	private final static int INTERVAL = 30; // 30 secs
	private final static int TOLERANCE = 30; // 30 secs
	// private final static int INTERVAL_MILLIS = INTERVAL * 1000;
	// private final static String ALGO_SHA1 = "SHA1";
	private final static String PASS_DELIMITER = "#";
	private final static String DEFAULT_ENCODING = "UTF-8";
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	/** The Constant PAD_ZERO. */
	private static final String PAD_ZERO = "0";

	/** The Constant MD5. */
	private static final String MD5 = "MD5";

	/** The Constant SHA1. */
	private static final String SHA1 = "SHA1";

	/** The Constant SHA2. */
	private static final String SHA2 = "SHA-256";

	/**
	 * 
	 * @param interval
	 *            : In Seconds
	 * @param secretKey
	 * @param message
	 * @param currentTime
	 *            : In MilliSeconds
	 * @return
	 */
	public static String generateHMAC(long interval, String secretKey, String message, long currentTime) {
		try {

			// Long epoch = Math.round(currentTime / 1000.0);

			String elapsed = Long.toString(currentTime / (interval * 1000));
			String password = String.join(PASS_DELIMITER, elapsed, secretKey, message);
			// System.out.println(interval + " " + secretKey + " " + message + " " +
			// currentTime + " " + password);
			MessageDigest md = MessageDigest.getInstance(SHA2);
			ByteArrayOutputStream pwsalt = new ByteArrayOutputStream();
			pwsalt.write(password.getBytes(DEFAULT_ENCODING));
			byte[] unhashedBytes = pwsalt.toByteArray();
			byte[] digestVonPassword = md.digest(unhashedBytes);
			return bytesToHex(digestVonPassword);
		} catch (NoSuchAlgorithmException | IOException e) {
			LOGGER.error("HMAC Error", e);
		}
		return null;
	}

	/**
	 * 
	 * @param interval
	 *            in seconds
	 * @param secretKey
	 * @param message
	 * @return
	 */
	public static String generateHMAC(long interval, String secretKey, String message) {
		String publicToken = generateHMAC(interval, secretKey, message, System.currentTimeMillis());
		return publicToken;
	}

	public static String generateHMAC(String secretKey, String message, long currentTime) {
		return generateHMAC(INTERVAL, secretKey, message, currentTime);
	}

	public static String generateHMAC(String secretKey, String message) {
		return generateHMAC(secretKey, message, System.currentTimeMillis());
	}

	public static boolean validateHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String hash) {
		LOGGER.debug("validateHMAC I:{} S:{} M:{} C:{} H:{} T:{}", interval, secretKey, message, currentTime, hash,
				tolerance);
		if (generateHMAC(interval, secretKey, message).equals(hash)) {
			return true;
		} else if (generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000).equals(hash)) {
			return true;
		} else if (generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000).equals(hash)) {
			return true;
		}
		return false;
	}

	public static boolean validateNumHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String numHash) {
		LOGGER.debug("validateHMAC I:{} S:{} M:{} C:{} H:{} T:{}", interval, secretKey, message, currentTime, numHash,
				tolerance);
		if (toNumeric(numHash.length(), generateHMAC(interval, secretKey, message)).equals(numHash)) {
			return true;
		} else if (toNumeric(numHash.length(),
				generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000)).equals(numHash)) {
			return true;
		} else if (toNumeric(numHash.length(),
				generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000)).equals(numHash)) {
			return true;
		}
		return false;
	}

	public static boolean validateComplexHMAC(long currentTime, long interval, long tolerance, String secretKey,
			String message, String complexHash) {

		LOGGER.debug("validateHMAC I:{} S:{} M:{} C:{} H:{} T:{}", interval, secretKey, message, currentTime,
				complexHash, tolerance);

		if (toComplex(complexHash.length(), generateHMAC(interval, secretKey, message)).equals(complexHash)) {
			return true;
		} else if (toComplex(complexHash.length(),
				generateHMAC(interval, secretKey, message, currentTime - tolerance * 1000)).equals(complexHash)) {
			return true;
		} else if (toNumeric(complexHash.length(),
				generateHMAC(interval, secretKey, message, currentTime + tolerance * 1000)).equals(complexHash)) {
			return true;
		}
		return false;
	}

	@Deprecated
	public static boolean validateHMAC(long interval, String secretKey, String message, long currentTime, String hash) {
		return validateHMAC(currentTime, interval, interval, secretKey, message, hash);
	}

	public static boolean validateHMAC(long interval, long tolerance, String secretKey, String message, String hash) {
		return validateHMAC(System.currentTimeMillis(), interval, tolerance, secretKey, message, hash);
	}

	@Deprecated
	public static boolean validateHMAC(long interval, String secretKey, String message, String hash) {
		return validateHMAC(interval, secretKey, message, System.currentTimeMillis(), hash);
	}

	public static boolean validateHMAC(String secretKey, String message, String publicToken) {
		return validateHMAC(INTERVAL, TOLERANCE, secretKey, message, publicToken);
	}

	public static String toNumeric(int length, String hash) {
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		long hashCode = Math.max(totalInt % Math.round(Math.pow(10, length)), 2);
		int passLenDiff = (length - String.valueOf(hashCode).length());
		long passLenFill = Math.max(Math.round(Math.pow(10, passLenDiff)) - 1, 1);
		return ArgUtil.parseAsString(hashCode * passLenFill);
	}

	private static final String COMPLEX_CHARS = "!@#$%^&*?0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final int COMPLEX_CHARS_LEN = COMPLEX_CHARS.length();

	public static String toComplex(int length, String hash) {
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		int vlength = length * 2;
		long hashCode = Math.max(totalInt % Math.round(Math.pow(10, vlength)), 2);
		int passLenDiff = (vlength - String.valueOf(hashCode).length());
		long passLenFill = Math.max(Math.round(Math.pow(10, passLenDiff)) - 1, 1);
		long next = hashCode * passLenFill;
		StringBuilder complexHash = new StringBuilder();
		while (next > 0) {
			long thisIndex = next % 100;
			next = (next - thisIndex) / 100;
			thisIndex = thisIndex % COMPLEX_CHARS_LEN;
			complexHash.append(COMPLEX_CHARS.charAt((int) thisIndex));
		}
		return complexHash.toString();
	}

	public static String toHex(int length, String hash) {
		// length = length*2;
		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		long hashCode = Math.max(totalInt % Math.round(Math.pow(16, length)), 2);
		int passLenDiff = (length - String.valueOf(Long.toHexString(hashCode)).length());
		long passLenFill = Math.max(Math.round(Math.pow(16, passLenDiff)) - 1, 1);
		// return (Long.toHexString(hashCode * passLenFill));
		return (Long.toHexString(hashCode * passLenFill) + "FFFFFF").substring(0, length);
	}

	private static String bytesToHex(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	/**
	 * Gets the m d5 hash.
	 *
	 * @param str
	 *            the str
	 * @return md5 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getMD5Hash(String str) throws NoSuchAlgorithmException {
		return getMD5Hash(str.getBytes());
	}

	/**
	 * Gets the m d5 hash.
	 *
	 * @param byteArray
	 *            the byte array
	 * @return md5 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getMD5Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, MD5);
	}

	/**
	 * Gets the SH a1 hash.
	 *
	 * @param str
	 *            the str
	 * @return sha1 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getSHA1Hash(String str) throws NoSuchAlgorithmException {
		return getSHA1Hash(str.getBytes());
	}

	/**
	 * Gets the SH a1 hash.
	 *
	 * @param byteArray
	 *            the byte array
	 * @return sha1 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getSHA1Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, SHA1);
	}

	/**
	 * Gets the SH a2 hash.
	 *
	 * @param str
	 *            the str
	 * @return sha2 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getSHA2Hash(String str) throws NoSuchAlgorithmException {
		return getSHA2Hash(str.getBytes());
	}

	/**
	 * Gets the SH a2 hash.
	 *
	 * @param byteArray
	 *            the byte array
	 * @return sha2 hashed string
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	public static String getSHA2Hash(byte[] byteArray) throws NoSuchAlgorithmException {
		return getHashedStrFor(byteArray, SHA2);
	}

	/**
	 * Gets the hashed str for.
	 *
	 * @param byteArray
	 *            the byte array
	 * @param algorithm
	 *            the algorithm
	 * @return the hashed str for
	 * @throws NoSuchAlgorithmException
	 *             the no such algorithm exception
	 */
	private static String getHashedStrFor(byte[] byteArray, String algorithm) throws NoSuchAlgorithmException {

		MessageDigest msgDigest = MessageDigest.getInstance(algorithm);
		msgDigest.reset();
		msgDigest.update(byteArray);
		byte[] digest = msgDigest.digest();
		BigInteger bigInt = new BigInteger(1, digest);
		String hashtext = bigInt.toString(16);
		// Pad it to get full 32 chars.
		while (hashtext.length() < 32) {
			hashtext = PAD_ZERO + hashtext;
		}

		return hashtext;

	}

	public static class HashBuilder implements Serializable {
		private static final long serialVersionUID = 3866060536613924880L;
		private long interval;
		private long tolerance;
		private String secret;
		private String message;
		private long currentTime;
		private String output;
		private String hash;

		public HashBuilder() {
			this.currentTime = System.currentTimeMillis();
			this.interval = INTERVAL;
			this.tolerance = TOLERANCE;
		}

		/**
		 * Interval in seconds
		 * 
		 * @param interval
		 * @return
		 */
		public HashBuilder interval(long interval) {
			this.interval = interval;
			return this;
		}

		public HashBuilder tolerance(long tolerance) {
			this.tolerance = tolerance;
			return this;
		}

		/**
		 * Current Time stamp in milliseconds default taken from
		 * System.currentTimeMillis()
		 * 
		 * @param currentTime
		 * @return
		 */
		public HashBuilder currentTime(long currentTime) {
			this.currentTime = currentTime;
			return this;
		}

		public HashBuilder secret(String secret) {
			this.secret = secret;
			return this;
		}

		public HashBuilder hash(String hash) {
			this.hash = hash;
			return this;
		}

		public HashBuilder message(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Generates SHA2 based HASH from params
		 * 
		 * @return
		 */
		public HashBuilder toSHA2() {
			try {
				this.hash = CryptoUtil.getSHA2Hash(this.message);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			return this;
		}

		/**
		 * Generates time based HASH from params
		 * 
		 * @return
		 */
		public HashBuilder toHMAC() {
			this.hash = CryptoUtil.generateHMAC(this.interval, this.secret, this.message, this.currentTime);
			return this;
		}

		/**
		 * Converts generated has to Numeric Representation. Should be called post Any
		 * Hash Function only.
		 * 
		 * @param length
		 * @return
		 */
		public HashBuilder toNumeric(int length) {
			this.output = CryptoUtil.toNumeric(length, this.hash);
			return this;
		}

		public HashBuilder toComplex(int length) {
			this.output = CryptoUtil.toComplex(length, this.hash);
			return this;
		}

		public HashBuilder toHex(int length) {
			this.output = CryptoUtil.toHex(length, this.hash);
			return this;
		}

		public String hash() {
			return hash;
		}

		public String output() {
			return ArgUtil.isEmpty(this.output) ? this.hash : this.output;
		}

		public boolean validate(String hash) {
			// return CryptoUtil.validateHMAC(this.interval, this.secret, this.message,
			// this.currentTime, hash);

			return CryptoUtil.validateHMAC(this.currentTime, this.interval, this.tolerance, this.secret, this.message,
					hash);

		}

		public boolean validateNumHMAC(String numHash) {
			return CryptoUtil.validateNumHMAC(this.currentTime, this.interval, this.tolerance, this.secret,
					this.message, numHash);
		}

		public boolean validateComplexHMAC(String complexHash) {
			return CryptoUtil.validateComplexHMAC(this.currentTime, this.interval, this.tolerance, this.secret,
					this.message, complexHash);
		}

	}

}
