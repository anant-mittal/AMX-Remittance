package com.amx.jax.postman.service;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.bootloaderjs.ArgUtil;
import com.bootloaderjs.ContextUtil;

@Component
public class TemplateUtils {

	private Logger log = Logger.getLogger(getClass());

	public void reverseFlag(boolean set) {
		ContextUtil.map().put("reverseflag", true);
	}

	public boolean reverseFlag() {
		return ArgUtil.parseAsBoolean(ContextUtil.map().get("reverseflag"), false);
	}

	public String reverse(String str) {
		if (reverseFlag()) {
			return new StringBuilder(str).reverse().toString();
		}
		return str;
	}

	public String reverse() {
		return "-X-X-";
	}

}
