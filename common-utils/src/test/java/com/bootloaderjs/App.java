package com.bootloaderjs;

import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.amx.utils.CryptoUtil;
import com.amx.utils.StringUtils;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		long timeout = 100L;
		System.out.println("DIV=" + (TimeUnit.NANOSECONDS.toSeconds(timeout) - 1));
		// String lalit = "lalit.tanwar07@gmail.com";
		// String amit = "amitt.n.tanwar07@gmail.com";
		// System.out.println("HASH=" + StringUtils.hash(lalit, 99));
		// System.out.println("HASH=" + StringUtils.hash(amit, 99));

	}

	public static void generate() {
		System.out.println(CryptoUtil.generateHMAC("appd-kwt.amxremit.com", "traceId"));
	}

}
