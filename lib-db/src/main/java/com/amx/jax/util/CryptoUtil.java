package com.amx.jax.util;

import java.security.MessageDigest;

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

	public static void main(String[] args) {
		CryptoUtil util = new CryptoUtil();
		System.out.println(util.getHash("293111302791", "test"));
	}
}
