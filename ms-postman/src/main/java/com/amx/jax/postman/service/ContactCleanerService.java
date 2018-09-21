package com.amx.jax.postman.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.utils.StringUtils;

@Component
public class ContactCleanerService {

	int totaltEmails = 0;
	int totaltMobiles = 0;
	int totaltWhatsApp = 0;
	Map<String, String> mapEmails = new HashMap<String, String>();
	Map<String, String> mapMobiles = new HashMap<String, String>();
	Map<String, String> mapWhatsApp = new HashMap<String, String>();

	public ContactCleanerService(@Value("${app.test.email}") List<String> appTestEmails,
			@Value("${app.test.mobile}") List<String> appTestMobile,
			@Value("${app.test.whatsapp}") List<String> appTestWhatsApp) {
		totaltEmails = appTestEmails.size();
		for (int e = 0; e < totaltEmails; e++) {
			mapEmails.put(String.valueOf(e), appTestEmails.get(e));
			mapEmails.put(appTestEmails.get(e), appTestEmails.get(e));
		}
		totaltMobiles = appTestMobile.size();
		for (int m = 0; m < totaltMobiles; m++) {
			mapMobiles.put(String.valueOf(m), appTestMobile.get(m));
			mapMobiles.put(appTestMobile.get(m), appTestMobile.get(m));
		}
		totaltWhatsApp = appTestWhatsApp.size();
		for (int w = 0; w < totaltWhatsApp; w++) {
			mapMobiles.put(String.valueOf(w), appTestWhatsApp.get(w));
			mapMobiles.put(appTestWhatsApp.get(w), appTestWhatsApp.get(w));
		}
	}

	public String getMobile(String mobile) {
		if (totaltMobiles > 0) {
			if (mapMobiles.containsKey(mobile)) {
				return mobile;
			}
			String hash = String.valueOf(StringUtils.hash(mobile, totaltMobiles));
			return mapMobiles.get(hash);
		}
		return mobile;
	}

	public String getEmail(String email) {
		if (totaltEmails > 0) {
			if (mapEmails.containsKey(email)) {
				return email;
			}
			String hash = String.valueOf(StringUtils.hash(email, totaltEmails));
			return mapEmails.get(hash);
		}
		return email;
	}

	public String getWhatsApp(String whatsApp) {
		if (totaltWhatsApp > 0) {
			if (mapWhatsApp.containsKey(whatsApp)) {
				return whatsApp;
			}
			String hash = String.valueOf(StringUtils.hash(whatsApp, totaltWhatsApp));
			return mapWhatsApp.get(hash);
		}
		return whatsApp;
	}

	public String[] getEmail(List<String> tos) {
		String[] array = new String[tos.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = getEmail(tos.get(i));
		}
		return array;
	}

}
