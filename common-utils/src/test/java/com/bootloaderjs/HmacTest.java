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
		System.out.println(CryptoUtil.generateHMAC("secret", "message"));
	}

}
