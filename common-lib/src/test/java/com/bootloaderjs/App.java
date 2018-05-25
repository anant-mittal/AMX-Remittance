package com.bootloaderjs;

import java.util.regex.Pattern;

import com.amx.jax.AppParam;
import com.amx.utils.JsonUtil;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^([A-Z]{3})-([\\w]+)-(\\w+)$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("====" + JsonUtil.toJson(AppParam.values()));
	}
}
