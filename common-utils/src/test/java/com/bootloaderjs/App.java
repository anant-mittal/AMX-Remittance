package com.bootloaderjs;

import java.util.regex.Pattern;

import com.amx.utils.ArgUtil;
import com.amx.utils.CryptoUtil;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println();
		// generate();
		System.out.println(ArgUtil.areEqual(" ", " "));
	}

	public static void generate() {
		System.out.println(CryptoUtil.generateHMAC("appd-kwt.amxremit.com", "traceId"));
	}

}
