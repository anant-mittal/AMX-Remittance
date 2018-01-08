package com.amx.jax.payment.gateway;

public class PayGParams {
	String amount = null;
	String trackId = null;
	String dockNo = null;
	String name = null;
	String redirectUrl = null;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTrackId() {
		return trackId;
	}

	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	public String getDockNo() {
		return dockNo;
	}

	public void setDockNo(String dockNo) {
		this.dockNo = dockNo;
	}

}
