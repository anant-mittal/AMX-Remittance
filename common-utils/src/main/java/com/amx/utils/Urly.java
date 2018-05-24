package com.amx.utils;

import java.net.MalformedURLException;
import java.net.URL;

public class Urly {

	public static String getDomainName(String url) throws MalformedURLException {
		if (!url.startsWith("http") && !url.startsWith("https")) {
			url = "http://" + url;
		}
		URL netUrl = new URL(url);
		String host = netUrl.getHost();
		if (host.startsWith("www")) {
			host = host.substring("www".length() + 1);
		}
		return host;
	}

	public static String getSubDomainName(String url) throws MalformedURLException {
		String[] names = url.split("\\.");
		if (names.length < 3) {
			return null;
		} else {
			return names[0];
		}
	}

	public static URLBuilder getBuilder() {
		return new URLBuilder();
	}

	public static URLBuilder parse(String urlString) throws MalformedURLException {
		URL url = new URL(urlString);
		URLBuilder builder = new URLBuilder(url.getAuthority());
		builder.setPath(url.getPath());
		builder.setConnectionType(url.getProtocol());
		builder.addParameter(url.getQuery());
		return builder;
	}
}
