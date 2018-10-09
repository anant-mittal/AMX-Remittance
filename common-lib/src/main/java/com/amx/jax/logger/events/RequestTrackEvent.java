package com.amx.jax.logger.events;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.amx.jax.AppContext;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.tunnel.TunnelMessage;
import com.amx.utils.HttpUtils;
import com.fasterxml.jackson.annotation.JsonGetter;

public class RequestTrackEvent extends AuditEvent {

	private static final long serialVersionUID = -8735500343787196557L;
	private static final Logger LOGGER = LoggerFactory.getLogger(RequestTrackEvent.class);

	public static enum Type implements EventType {
		REQT_IN, RESP_OUT, REQT_OUT, RESP_IN, PUB_OUT, SUB_IN;

		@Override
		public EventMarker marker() {
			return null;
		}
	}

	private MultiValueMap<String, String> header;
	private AppContext context;
	private long responseTime;
	private String ip;

	public MultiValueMap<String, String> getHeader() {
		return header;
	}

	public void setHeader(MultiValueMap<String, String> header) {
		this.header = header;
	}

	public RequestTrackEvent(Type type) {
		super(type);
	}

	public <T> RequestTrackEvent(Type type, TunnelMessage<T> message) {
		super(type);
		this.description = String.format("%s %s", this.type, message.getTopic());
		this.context = message.getContext();
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

	public RequestTrackEvent(HttpServletResponse resp, HttpServletRequest req, long responseTime) {
		this(resp, req);
		this.responseTime = responseTime;
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
		this.ip = HttpUtils.getIPAddress(request);
		return this;
	}

	public RequestTrackEvent track(HttpRequest request) {
		this.description = String.format("%s %s=%s", this.type, request.getMethod(), request.getURI());
		// this.header = request.getHeaders();

		this.header = new LinkedMultiValueMap<String, String>();
		Collection<Entry<String, List<String>>> headers = request.getHeaders().entrySet();
		for (Entry<String, List<String>> header : headers) {
			this.header.put(header.getKey(), header.getValue());
		}
		return this;
	}

	public RequestTrackEvent track(ClientHttpResponse response, URI uri) {
		try {
			this.description = String.format("%s %s=%s", this.type, response.getStatusCode(), uri);
		} catch (IOException e) {
			LOGGER.error("RequestTrackEvent.track while logging response in", e);
			this.description = String.format("%s %s=%s", this.type, "EXCEPTION", uri);
		}
		// this.header = response.getHeaders();
		this.header = new LinkedMultiValueMap<String, String>();
		Collection<Entry<String, List<String>>> headers = response.getHeaders().entrySet();
		for (Entry<String, List<String>> header : headers) {
			this.header.put(header.getKey(), header.getValue());
		}
		return this;
	}

	public long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(long responseTime) {
		this.responseTime = responseTime;
	}

	public AppContext getContext() {
		return context;
	}

	public void setContext(AppContext context) {
		this.context = context;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void clean() {
		if (this.header != null) {
			this.header.remove("connection");
			this.header.remove("accept");
			this.header.remove("Accept");
			this.header.remove("accept-encoding");
			this.header.remove("accept-language");
			this.header.remove("Content-Length");
			this.header.remove("X-Application-Context");
			this.header.remove("Content-Type");
			this.header.remove("Transfer-Encoding");
			this.header.remove("Date");
			this.header.remove("Connection");
		}
	}

}
