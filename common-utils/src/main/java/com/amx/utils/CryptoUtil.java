package com.amx.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CryptoUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

	private final static int interval = 30;
	private final static String ALGO_SHA1 = "SHA1";
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

	public static String generateHMAC(String publicKey, String message, long currentTime) {
		try {
			Long epoch = Math.round(currentTime / 1000.0);
			String elapsed = Long.toString(epoch / interval);
			String password = String.join(PASS_DELIMITER, elapsed, publicKey, message);
			MessageDigest md = MessageDigest.getInstance(ALGO_SHA1);
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

	public static String generateHMAC(String publicKey, String message) {
		return generateHMAC(publicKey, message, System.currentTimeMillis());
	}

	public static boolean validateHMAC(String publicKey, String publicToken, String message) {
		if (generateHMAC(publicKey, message).equals(publicToken)) {
			return true;
		} else if (generateHMAC(publicKey, message, System.currentTimeMillis() - interval).equals(publicToken)) {
			return true;
		} else if (generateHMAC(publicKey, message, System.currentTimeMillis() + interval).equals(publicToken)) {
			return true;
		}
		return false;
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
		return getSHA1Hash(str.getBytes());
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

}
