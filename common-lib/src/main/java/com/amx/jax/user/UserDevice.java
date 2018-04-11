package com.amx.jax.user;

public class UserDevice {

	public enum AppType {
		WEB, ANDROID, IOS;
	}

	protected String fingerprint = null;
	protected String ip = null;
	protected String id = null;
	protected String appVersion = null;
	protected AppType appType = null;

	public String getFingerprint() {
		return fingerprint;
	}

	public String getId() {
		return id;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public void setFingerprint(String fingerprint) {
		this.fingerprint = fingerprint;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}

}
