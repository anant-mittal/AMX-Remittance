package com.amx.jax.user;

import java.io.Serializable;

import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.Version;

/**
 * 
 * @author lalittanwar
 *
 */
public class UserDevice implements Serializable {

	private static final long serialVersionUID = 4015555971724271185L;

	public enum AppType {
		WEB, ANDROID, IOS;
	}

	protected String fingerprint = null;
	protected String ip = null;
	protected String id = null;
	protected String appVersion = null;
	protected AppType appType = null;

	protected DeviceType type = null;
	protected DevicePlatform platform = null;
	protected OperatingSystem operatingSystem = null;
	protected Browser browser = null;
	protected Version browserVersion = null;

	/**
	 * THis is unique id sent by device, in case of mobiles it is device id, in case
	 * of browser it is unique id generated to identify browser. Its value depends
	 * on values provided by client. can be compromised.
	 * 
	 * @return
	 */
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * 
	 * unique id generated on server, it is less accurate than fingerprint, A new id
	 * is generated if device id is not identified, by us.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Version of mobile app used by user.
	 * 
	 * @return
	 */
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

	/**
	 * 
	 * Enumeration for the type of device that has been resolved
	 * 
	 * NORMAL, MOBILE, TABLET.
	 * 
	 * @see org.springframework.mobile.device.DeviceType
	 * 
	 * @return
	 */
	public DeviceType getType() {
		return type;
	}

	public void setType(DeviceType type) {
		this.type = type;
	}

	/**
	 * Enumeration for the platform of device that has been resolved
	 * 
	 * IOS, ANDROID, UNKNOWN
	 * 
	 * @see org.springframework.mobile.device.DevicePlatform
	 * 
	 * @return
	 */
	public DevicePlatform getPlatform() {
		return platform;
	}

	public void setPlatform(DevicePlatform platform) {
		this.platform = platform;
	}

	/**
	 * @see eu.bitwalker.useragentutils.OperatingSystem
	 */
	public OperatingSystem getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	/**
	 * @see eu.bitwalker.useragentutils.Browser
	 */
	public Browser getBrowser() {
		return browser;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public Version getBrowserVersion() {
		return browserVersion;
	}

	public void setBrowserVersion(Version browserVersion) {
		this.browserVersion = browserVersion;
	}

	/**
	 * Get IP Address of user deivce
	 * 
	 * @return
	 */
	public String getIp() {
		return ip;
	}

}
