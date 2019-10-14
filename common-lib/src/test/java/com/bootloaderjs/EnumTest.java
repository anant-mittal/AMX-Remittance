package com.bootloaderjs;

import java.text.ParseException;

import com.amx.jax.dict.Language;
import com.amx.utils.ArgUtil;

public class EnumTest { // Noncompliant

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static void main(String[] args) throws ParseException {
		Language l = (Language) ArgUtil.parseAsEnum("l", null, Language.class);
		System.out.println("lang=== " + l);
	}

}
