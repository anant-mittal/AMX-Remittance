package com.amx.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CryptoUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(CryptoUtil.class);

	private final static int interval = 30;
	private final static String ALGO_SHA1 = "SHA1";
	private final static String PASS_DELIMITER = "#";
	private final static String DEFAULT_ENCODING = "UTF-8";
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String generateHMAC(String publicKey, long currentTime) {
		try {
			Long epoch = Math.round(currentTime / 1000.0);
			String elapsed = Long.toString(epoch / interval);
			String password = String.join(PASS_DELIMITER, elapsed, publicKey);
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

	public static String generateHMAC(String publicKey) {
		return generateHMAC(publicKey, System.currentTimeMillis());
	}

	public static boolean validateHMAC(String publicKey, String message) {
		if (generateHMAC(publicKey).equals(message)) {
			return true;
		} else if (generateHMAC(publicKey, System.currentTimeMillis() - interval).equals(message)) {
			return true;
		} else if (generateHMAC(publicKey, System.currentTimeMillis() + interval).equals(message)) {
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
}
