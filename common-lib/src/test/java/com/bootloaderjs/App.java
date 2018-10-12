package com.bootloaderjs;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.amx.utils.Urly;

public class App { // Noncompliant

	public static final Pattern pattern = Pattern.compile("^\\$\\{(.*)\\}$");

	/**
	 * This is just a test method
	 * 
	 * @param args
	 * @throws MalformedURLException
	 * @throws URISyntaxException
	 */
	public static void main(String[] args) throws MalformedURLException, URISyntaxException {
		Matcher match = pattern.matcher("${app.prod}");

		System.out.println(Urly.parse("https://lalittanwar.com/sso/login/DONE").addParameter("some", "value").getURL());
		System.out.println(Urly.parse("/sso/login/DONE").addParameter("some", "value").getURL());

	}
}
