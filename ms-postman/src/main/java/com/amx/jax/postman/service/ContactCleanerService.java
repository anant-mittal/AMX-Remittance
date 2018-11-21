package com.amx.jax.postman.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.utils.ArgUtil;
import com.amx.utils.StringUtils;

@Component
public class ContactCleanerService {

	int totaltEmails = 0;
	int totaltMobiles = 0;
	int totaltWhatsApp = 0;
	Map<String, String> mapEmails = new HashMap<String, String>();
	Map<String, String> mapMobiles = new HashMap<String, String>();
	Map<String, String> mapWhatsApp = new HashMap<String, String>();

	public ContactCleanerService(@Value("${app.test.email}") String[] appTestEmails,
			@Value("${app.test.mobile}") String[] appTestMobile,
			@Value("${app.test.whatsapp}") String[] appTestWhatsApp) {
		totaltEmails = 0;
		for (int e = 0; e < appTestEmails.length; e++) {
			String emailId = appTestEmails[e];
			if (!ArgUtil.isEmpty(emailId)) {
				mapEmails.put(String.valueOf(totaltEmails), emailId);
				mapEmails.put(emailId, emailId);
				totaltEmails++;
			}
		}
		totaltMobiles = 0;
		for (int m = 0; m < appTestMobile.length; m++) {
			String mobileId = appTestMobile[m];
			if (!ArgUtil.isEmpty(mobileId)) {
				mapMobiles.put(String.valueOf(totaltMobiles), mobileId);
				mapMobiles.put(mobileId, mobileId);
				totaltMobiles++;
			}
		}
		totaltWhatsApp = 0;
		for (int w = 0; w < appTestWhatsApp.length; w++) {
			String waId = appTestWhatsApp[w];
			if (!ArgUtil.isEmpty(waId)) {
				mapWhatsApp.put(String.valueOf(totaltWhatsApp), waId);
				mapWhatsApp.put(waId, waId);
				totaltWhatsApp++;
			}
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
		int length = tos.size();
		String[] array = new String[length];
		for (int i = 0; i < length; i++) {
			array[i] = getEmail(tos.get(i));
		}
		return array;
	}

}
