package com.amx.jax.task.events;

import java.math.BigDecimal;

import com.amx.jax.tunnel.TunnelEvent;

public class GeoNotifyTask extends TunnelEvent {

	private static final long serialVersionUID = 7415624585226619390L;

	private BigDecimal customerId;
	private String geoPoint;
	private String appTitle;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getGeoPoint() {
		return geoPoint;
	}

	public void setGeoPoint(String geoPoint) {
		this.geoPoint = geoPoint;
	}

	public String getAppTitle() {
		return appTitle;
	}

	public void setAppTitle(String appTitle) {
		this.appTitle = appTitle;
	}

}
