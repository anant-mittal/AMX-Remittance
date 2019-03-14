package com.amx.jax.logger.events;

import java.io.IOException;
import java.net.URI;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;

import com.amx.jax.AppContext;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.tunnel.TunnelEventXchange;
import com.amx.jax.tunnel.TunnelMessage;
import com.amx.utils.HttpUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
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

	private AppContext context;
	@JsonProperty("rspTym")
	private long responseTime;
	private String ip;

	public RequestTrackEvent(Type type) {
		super(type);
	}

	public <T> RequestTrackEvent(Type type, TunnelEventXchange xchange, TunnelMessage<T> message) {
		super(type);
		this.description = String.format("%s %s=%s", this.type, xchange, message.getTopic());
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
		return this;
	}

	public RequestTrackEvent track(HttpServletRequest request) {
		this.description = String.format("%s %s=%s", this.type, request.getMethod(), request.getRequestURI());
		this.ip = HttpUtils.getIPAddress(request);
		return this;
	}

	public RequestTrackEvent track(HttpRequest request) {
		this.description = String.format("%s %s=%s", this.type, request.getMethod(), request.getURI());
		return this;
	}

	public RequestTrackEvent track(ClientHttpResponse response, URI uri) {
		try {
			this.description = String.format("%s %s=%s", this.type, response.getStatusCode(), uri);
		} catch (IOException e) {
			LOGGER.error("RequestTrackEvent.track while logging response in", e);
			this.description = String.format("%s %s=%s", this.type, "EXCEPTION", uri);
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
	}

}
