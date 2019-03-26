package com.amx.jax.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dal.CryptoDao;

@Component
public class CryptoUtil {

	private Logger log = Logger.getLogger(CryptoUtil.class);

	@Autowired
	private CryptoDao cryptoDao;

	public String decrypt(String saltKey, String encryptedValue) {
		return cryptoDao.decrypt(saltKey, encryptedValue);
	}

	public String encrypt(String saltKey, String decryptedValue) {
		return cryptoDao.encrypt(saltKey, decryptedValue);
	}

	public String getHash(String userid, String key) {

		byte[] output = null;
		String salt = null;
		String newpassoword = null;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			salt = new StringBuffer(userid).reverse().toString();
			newpassoword = null;
			salt = salt.substring(0, 4);
			newpassoword = addSalt(key, salt);
			md.update(newpassoword.getBytes());
			output = md.digest();
			newpassoword = bytesToHex(output);
		} catch (Exception e) {
			log.error("error in gethash", e);
		}
		return newpassoword;
	}
	
	public String generateHash(String salt, String key) {

		byte[] output = null;
		String newpassoword = null;

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			salt = new StringBuffer(salt).reverse().toString();
			newpassoword = null;
			newpassoword = addSalt(key, salt);
			md.update(newpassoword.getBytes());
			output = md.digest();
			newpassoword = bytesToHex(output);
		} catch (Exception e) {
			log.error("error in gethash", e);
		}
		return newpassoword;
	}

	public String addSalt(String password, String salt) {
		final int aLength = password.length();
		final int bLength = salt.length();
		final StringBuilder sb = new StringBuilder(aLength + bLength);
		try {
			final int min = Math.min(aLength, bLength);
			for (int i = 0; i < min; i++) {
				sb.append(password.charAt(i));
				sb.append(salt.charAt(i));
			}
			if (aLength > bLength) {
				sb.append(password, bLength, aLength);
			} else if (aLength < bLength) {
				sb.append(salt, aLength, bLength);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new NullPointerException("Invalid values : ");
		}
		return sb.toString();
	}

	public String bytesToHex(byte[] b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuffer buf = new StringBuffer();
		for (int j = 0; j < b.length; j++) {
			buf.append(hexDigit[(b[j] >> 4) & 0x0f]);
			buf.append(hexDigit[b[j] & 0x0f]);
		}
		return new String(buf.toString());
	}

	/* ------ CUSTOMER OTP ALGORITHM START HERE ------ */
	public String getSHA(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] messageDigest = md.digest(input.getBytes());
			BigInteger no = new BigInteger(1, messageDigest);
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			log.error("error in AES Decryption", e);
			return null;
		}
	}

	public static String base64Encode(String string) {
		Base64.Encoder encoder = Base64.getEncoder();
		return String.valueOf(encoder.encodeToString(string.getBytes()).toString());
	}

	public static String base64Decode(String encryptedString) {
		Base64.Decoder decoder = Base64.getDecoder();
		return new String(decoder.decode(encryptedString));
	}

	public static String cyclicLeftShift(String s, int k) {
		k = k % s.length();
		return s.substring(k) + s.substring(0, k);
	}

	public static String cyclicRightShift(String s, int k) {
		k = k % s.length();
		return s.substring(s.length() - k) + s.substring(0, s.length() - k);
	}

	public String encryptCOTP(String text, String key) {
		String base64 = base64Encode(text);
		String subBase64 = cyclicLeftShift(base64, 3);
		String hashedKey = getSHA(key);
		String hashedSubBase64String = subBase64 + hashedKey;
		String base64Encrypted = base64Encode(hashedSubBase64String);
		String finalEncrpytedValue = cyclicLeftShift(base64Encrypted, 6);
		return finalEncrpytedValue;
	}

	public String decryptCOTP(String finalEncrpytedValue, String key) {
		String base64Encrypted = cyclicRightShift(finalEncrpytedValue, 6);
		String hashedSubBase64String = base64Decode(base64Encrypted);
		String hashedKey = getSHA(key);
		String subBase64 = hashedSubBase64String.replace(hashedKey, "");
		String base64 = cyclicRightShift(subBase64, 3);
		return base64Decode(base64);
	}

	/* ------ CUSTOMER OTP ALGORITHM END HERE ------ */

	public static void main(String[] args) {
		CryptoUtil util = new CryptoUtil();
		System.out.println(util.getHash("293111302791", "test"));
		String encrypted = util.encryptCOTP("246246", "172");
		System.out.println("Encrypted :- " + encrypted);
		String decrpted = util.decryptCOTP(encrypted, "172");
		System.out.println("Decrypted :- " + decrpted);

	}
}
