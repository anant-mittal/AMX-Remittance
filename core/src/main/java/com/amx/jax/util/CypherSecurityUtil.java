package com.amx.jax.util;

import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CypherSecurityUtil {
	
	public String encodePassword(String rawSecret, String secretKey) {
		return new ShaPasswordEncoder(256).encodePassword(rawSecret, secretKey);
	}

}
