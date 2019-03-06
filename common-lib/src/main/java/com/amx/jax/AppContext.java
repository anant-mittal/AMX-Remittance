package com.amx.jax;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.amx.jax.dict.Tenant;
import com.amx.jax.dict.UserClient.UserDeviceClient;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppContext implements Serializable {

	private static final long serialVersionUID = -6073379040253976816L;

	Tenant tenant = null;
	String traceId = null;
	String tranxId = null;
	String actorId = null;
	UserDeviceClient client;
	Map<String, Object> params = new HashMap<String, Object>();

	long traceTime = 0L;
	long tranxTime = 0L;

	public Tenant getTenant() {
		return tenant;
	}

	public void setTenant(Tenant tenant) {
		this.tenant = tenant;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public String getTranxId() {
		return tranxId;
	}

	public void setTranxId(String tranxId) {
		this.tranxId = tranxId;
	}

	public String getActorId() {
		return actorId;
	}

	public void setActorId(String actorId) {
		this.actorId = actorId;
	}

	public long getTraceTime() {
		return traceTime;
	}

	public void setTraceTime(long traceTime) {
		this.traceTime = traceTime;
	}

	public long getTranxTime() {
		return tranxTime;
	}

	public void setTranxTime(long tranxTime) {
		this.tranxTime = tranxTime;
	}

	public UserDeviceClient getClient() {
		return client;
	}

	public void setClient(UserDeviceClient client) {
		this.client = client;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

}
