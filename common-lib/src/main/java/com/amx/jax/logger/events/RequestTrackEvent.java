package com.amx.jax.logger.events;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.jax.logger.AuditEvent;
import com.amx.utils.EnumType;

public class RequestTrackEvent extends AuditEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrackEvent.class);

	public static enum Type implements EnumType {
		REQT_IN, RESP_OUT, REQT_OUT, RESP_IN;
	}

	private MultiValueMap<String, String> header;

	public MultiValueMap<String, String> getHeader() {
		return header;
	}

	public void setHeader(MultiValueMap<String, String> header) {
		this.header = header;
	}

	public RequestTrackEvent(Type type) {
		super(type);
	}

	public RequestTrackEvent(HttpRequest request) {
		super(Type.REQT_OUT);
		this.track(request);
	}

	public RequestTrackEvent(ClientHttpResponse response, HttpRequest request) {
		super(Type.RESP_IN);
		this.track(response, request.getURI());
	}

	public RequestTrackEvent(HttpServletRequest request) {
		super(Type.REQT_IN);
		this.track(request);
	}

	public RequestTrackEvent(HttpServletResponse response, HttpServletRequest request) {
		super(Type.RESP_OUT);
		this.track(response, request);
	}

	public RequestTrackEvent track(HttpServletResponse response, HttpServletRequest request) {
		this.description = String.format("%s %s=%s", this.type, response.getStatus(), request.getRequestURI());
		this.header = new LinkedMultiValueMap<String, String>();

		Collection<String> headerNames = response.getHeaderNames();
		for (String headerName : headerNames) {
			List<String> values = (List<String>) response.getHeaders(headerName);
			header.put(headerName, values);
		}
		return this;
	}

	public RequestTrackEvent track(HttpServletRequest request) {
		this.description = String.format("%s %s=%s", this.type, request.getMethod(), request.getRequestURI());
		this.header = new LinkedMultiValueMap<String, String>();
		Enumeration<String> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			Enumeration<String> headerValues = request.getHeaders(headerName);
			while (headerValues.hasMoreElements()) {
				String headerValue = headerValues.nextElement();
				header.add(headerName, headerValue);
			}
		}
		return this;
	}

	public RequestTrackEvent track(HttpRequest request) {
		this.description = String.format("%s %s=%s", this.type, request.getMethod(), request.getURI());
		this.header = request.getHeaders();
		return this;
	}

	public RequestTrackEvent track(ClientHttpResponse response, URI uri) {
		try {
			this.description = String.format("%s %s=%s", this.type, response.getStatusCode(), uri);
		} catch (IOException e) {
			LOGGER.error("RequestTrackEvent.track while logging response out", e);
			this.description = String.format("%s %s=%s", this.type, "EXCEPTION", uri);
		}
		this.header = response.getHeaders();
		return this;
	}

}