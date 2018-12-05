package com.bootloaderjs;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.amx.utils.CryptoUtil.HashBuilder;
import com.amx.utils.Random;

public class HmacTest { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^com.amx.jax.logger.client.AuditFilter<(.*)>$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (int i = 0; i < 999999; i++) {
			String otp = new HashBuilder().secret("secret").message(Random.randomAlphaNumeric(6)).interval(1).toHMAC()
					.toNumeric(6)
					.output();

			// String otp = Random.randomAlphaNumeric(6);
			Integer count = map.getOrDefault(otp, 0);
			count++;
			map.put(otp, count);
		}
		Map<Integer, Integer> counter = new HashMap<Integer, Integer>();
		for (Entry<String, Integer> entry : map.entrySet()) {
			// System.out.println(String.format("%s x%s", entry.getKey(),
			// entry.getValue()));
			Integer count = counter.getOrDefault(entry.getValue(), 0);
			counter.put(entry.getValue(), ++count);
			// System.out.println(String.format("%s > %s", entry.getValue(), count));
		}

		for (Entry<Integer, Integer> entry : counter.entrySet()) {
			System.out.println(String.format("x%s v%s", entry.getKey(), entry.getValue()));
		}
	}

}
