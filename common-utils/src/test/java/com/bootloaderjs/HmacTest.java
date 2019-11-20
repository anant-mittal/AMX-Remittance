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
		
		//validateHMAC I:60 S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:5KE3ZQ C:1573114585707 H:3 T:60
		//validateHMAC I:60 S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:5KE3ZQ C:1573114585707 H:3 T:60
		
		//validateHMAC I:60 S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369 M:EET8AP C:1573112719915 H:r T:60
//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "3"));
		
		System.out.println(CryptoUtil.validateNumHMAC(1573114585707L, 60L, 60,
				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "3"));
		
//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "zmBHL7"));
//		
//		System.out.println(CryptoUtil.validateNumHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "zmBHL7"));
//		
//		System.out.println(CryptoUtil.validateComplexHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "S59hL6"));
//		
//		System.out.println(CryptoUtil.validateNumHMAC(1573114585707L, 60L, 60,
//				"S:b9dbe6a92c73cbe137b81bdd322cc0655655cd2bc0a782609283cddab528f369", "5KE3ZQ", "S59hL6"));
	}

}
