package com.amx.jax.util;

import org.springframework.stereotype.Component;

@Component
public class Util {

	public String createRandomPassword(int length) {
		String validChars = "1234567890";
		String password = "";
		for (int i = 0; i < length; i++) {
			password = password + String.valueOf(validChars.charAt((int) (Math.random() * validChars.length())));
		}
		return password;
	}

}
