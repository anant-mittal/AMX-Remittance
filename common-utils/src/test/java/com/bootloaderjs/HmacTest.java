package com.bootloaderjs;

import java.util.regex.Pattern;

import com.amx.utils.CryptoUtil;

public class HmacTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		int passLen = 6;

		String hash = CryptoUtil.generateHMAC(1, "secret", "message");
		System.out.println(hash);

		char[] hashChars = hash.toCharArray();
		int totalInt = 0;
		for (int i = 0; i < hashChars.length; i++) {
			int cint = hashChars[i];
			totalInt = (cint * cint * i) + totalInt;
		}
		long hashCode = (totalInt % Math.round(Math.pow(10, passLen)));
		int passLenDiff = (passLen - String.valueOf(hashCode).length());
		long passLenFill = Math.round(Math.pow(10, passLenDiff)) - 1;

		System.out.println(hashCode + "*" +passLenFill +" "+ (hashCode * passLenFill));

	}

}
