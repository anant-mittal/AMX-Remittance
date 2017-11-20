package com.amx.jax.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dal.CryptoDao;

@Component
public class CryptoUtil {

	@Autowired
	private CryptoDao cryptoDao;

	public String decrypt(String saltKey, String encryptedValue) {
		return cryptoDao.decrypt(saltKey, encryptedValue);
	}

	public String encrypt(String saltKey, String decryptedValue) {
		return cryptoDao.encrypt(saltKey, decryptedValue);
	}
}
