package com.amx.jax;

import java.io.Serializable;

import com.amx.jax.dict.Tenant;

public class AppContext implements Serializable {

	private static final long serialVersionUID = -6073379040253976816L;

	Tenant tenant = null;
	String traceId = null;
	String tranxId = null;
	String userId = null;

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
