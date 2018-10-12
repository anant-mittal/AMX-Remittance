package com.amx.utils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * The Class URLBuilder.
 */
public class URLBuilder {

	/** The params. */
	private StringBuilder folders, params;

	/** The host. */
	private String connType, host;

	/**
	 * Sets the connection type.
	 *
	 * @param conn
	 *            the new connection type
	 */
	void setConnectionType(String conn) {
		connType = conn;
	}

	/**
	 * Instantiates a new URL builder.
	 */
	public URLBuilder() {
		folders = new StringBuilder();
		params = new StringBuilder();
	}

	/**
	 * Instantiates a new URL builder.
	 *
	 * @param host
	 *            the host
	 */
	public URLBuilder(String host) {
		this();
		this.host = host;
	}

	/**
	 * Sets the path.
	 *
	 * @param folder
	 *            the folder
	 * @return the URL builder
	 */
	public URLBuilder setPath(String folder) {
		folders.append("/");
		folders.append(folder);
		return this;
	}

	/**
	 * Adds the parameter.
	 *
	 * @param query
	 *            the query
	 * @return the URL builder
	 */
	public URLBuilder addParameter(String query) {
		if (!ArgUtil.isEmptyString(query)) {
			if (params.toString().length() > 0) {
				params.append("&");
			}
			params.append(query);
		}
		return this;
	}

	/**
	 * Adds the parameter.
	 *
	 * @param parameter
	 *            the parameter
	 * @param value
	 *            the value
	 * @return the URL builder
	 */
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

	public URLBuilder addPathVariable(String parameter, Object value) {
		String valueStr = ArgUtil.parseAsString(value, "");
		String path = folders.toString();
		path.replace("{" + parameter + "}", valueStr);
		folders = new StringBuilder(path);
		return this;
	}

	/**
	 * Gets the url.
	 *
	 * @return the url
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 */
	public String getURL() throws URISyntaxException, MalformedURLException {
		URI uri;
		if (host == null) {
			// uri = new URI(null, null, folders.toString(), params.toString(), null);
			return folders.toString().replaceAll("/+", "/") + "?" + params.toString();
		} else if (connType == null) {
			// uri = new URI(null, null, folders.toString(), params.toString(), null);
			return host + folders.toString().replaceAll("/+", "/") + "?" + params.toString();
		} else {
			uri = new URI(connType, host, folders.toString().replaceAll("/+", "/"), params.toString(), null);
			return uri.toURL().toString();
		}
	}

	/**
	 * Gets the relative URL.
	 *
	 * @return the relative URL
	 * @throws URISyntaxException
	 *             the URI syntax exception
	 * @throws MalformedURLException
	 *             the malformed URL exception
	 */
	public String getRelativeURL() throws URISyntaxException, MalformedURLException {
		URI uri = new URI(null, null, folders.toString().replaceAll("/+", "/"), params.toString(), null);
		return uri.toString();
	}

}
