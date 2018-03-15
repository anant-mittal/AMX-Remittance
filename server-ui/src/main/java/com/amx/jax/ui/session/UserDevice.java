package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.ui.service.HttpService;
import com.bootloaderjs.ArgUtil;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDevice implements Serializable {

	private static final long serialVersionUID = -6869375666742059912L;

	private String fingerprint = null;
	private String ip = null;
	private String id = null;
	private DeviceType type = null;
	private DevicePlatform platform = null;
	private OperatingSystem operatingSystem = null;
	private Browser browser = null;
	private Version browserVersion = null;
	private String appVersion = null;

	@Autowired
	private HttpService httpService;

	private void resolve() {
		Device currentDevice = httpService.getCurrentDevice();
		this.ip = httpService.getIPAddress();
		this.type = (currentDevice.isMobile() ? DeviceType.MOBILE
				: (currentDevice.isTablet() ? DeviceType.TABLET : DeviceType.NORMAL));
		this.platform = currentDevice.getDevicePlatform();
		this.fingerprint = httpService.getDeviceId();
		UserAgent userAgent = httpService.getUserAgent();
		this.id = ArgUtil.parseAsString(userAgent.getId());
		this.browser = userAgent.getBrowser();
		this.browserVersion = userAgent.getBrowserVersion();
		this.operatingSystem = userAgent.getOperatingSystem();
	}

	public String getIp() {
		if (type == null) {
			this.resolve();
		}
		return ip;
	}

	public DeviceType getType() {
		if (type == null) {
			this.resolve();
		}
		return type;
	}

	public DevicePlatform getPlatform() {
		if (type == null) {
			this.resolve();
		}
		return platform;
	}

	public String getFingerprint() {
		if (type == null) {
			this.resolve();
		}
		return fingerprint;
	}

	public OperatingSystem getOperatingSystem() {
		if (type == null) {
			this.resolve();
		}
		return operatingSystem;
	}

	public Browser getBrowser() {
		if (type == null) {
			this.resolve();
		}
		return browser;
	}

	public Version getBrowserVersion() {
		if (type == null) {
			this.resolve();
		}
		return browserVersion;
	}

	public String getId() {
		if (type == null) {
			this.resolve();
		}
		return id;
	}

	public String getAppVersion() {
		if (type == null) {
			this.resolve();
		}
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", getId());
		map.put("fingerprint", fingerprint);
		map.put("platform", platform);
		map.put("type", type);
		map.put("ip", ip);
		map.put("browser", browser);
		map.put("browserVersion", browserVersion);
		map.put("os", operatingSystem);
		map.put("appVersion", appVersion);
		return map;
	}

	public UserDevice toUserDevice() {
		UserDevice device = new UserDevice();
		device.setId(getId());
		device.setFingerprint(getFingerprint());
		device.setPlatform(getPlatform());
		device.setType(getType());
		device.setIp(getIp());
		device.setBrowser(getBrowser());
		device.setBrowserVersion(getBrowserVersion());
		device.setOperatingSystem(getOperatingSystem());
		device.setAppVersion(getAppVersion());
		return device;
	}

	public void setBrowserVersion(Version browserVersion) {
		this.browserVersion = browserVersion;
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

	public void setType(DeviceType type) {
		this.type = type;
	}

	public void setPlatform(DevicePlatform platform) {
		this.platform = platform;
	}

	public void setOperatingSystem(OperatingSystem operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}
}
