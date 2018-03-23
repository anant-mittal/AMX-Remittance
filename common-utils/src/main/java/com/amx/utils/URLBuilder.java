package com.amx.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class URLBuilder {
	private StringBuilder folders, params;
	private String connType, host;

	void setConnectionType(String conn) {
		connType = conn;
	}

	public URLBuilder() {
		folders = new StringBuilder();
		params = new StringBuilder();
	}

	public URLBuilder(String host) {
		this();
		this.host = host;
	}

	public URLBuilder setPath(String folder) {
		folders.append("/");
		folders.append(folder);
		return this;
	}

	public URLBuilder addParameter(String parameter, Object value) {
		String valueStr = ArgUtil.parseAsString(value, "");
		if (params.toString().length() > 0) {
			params.append("&");
		}
		params.append(parameter);
		params.append("=");
		params.append(valueStr);
		return this;
	}

	public String getURL() throws URISyntaxException, MalformedURLException {
		URI uri;
		if (connType == null) {
			// uri = new URI(null, null, folders.toString(), params.toString(), null);
			return host + folders.toString() + "?" + params.toString();
		} else {
			uri = new URI(connType, host, folders.toString(), params.toString(), null);
			return uri.toURL().toString();
		}

	}

	public String getRelativeURL() throws URISyntaxException, MalformedURLException {
		URI uri = new URI(null, null, folders.toString(), params.toString(), null);
		return uri.toString();
	}
}
