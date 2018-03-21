package com.amx.jax.ui.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.ui.UIConstants;
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

	public enum AppType {
		WEB, ANDROID, IOS;
	}

	private String fingerprint = null;
	private String ip = null;
	private String id = null;
	private DeviceType type = null;
	private DevicePlatform platform = null;
	private OperatingSystem operatingSystem = null;
	private Browser browser = null;
	private Version browserVersion = null;
	private String appVersion = null;
	private AppType appType = null;

	@Autowired
	private HttpService httpService;

	public UserDevice resolve() {
		Device currentDevice = httpService.getCurrentDevice();
		this.ip = httpService.getIPAddress();
		this.type = (currentDevice.isMobile() ? DeviceType.MOBILE
				: (currentDevice.isTablet() ? DeviceType.TABLET : DeviceType.NORMAL));
		this.platform = currentDevice.getDevicePlatform();
		this.fingerprint = httpService.getDeviceId();
		UserAgent userAgent = httpService.getUserAgent();
		if (this.id == null) {
			String idn = null;
			if (this.fingerprint != null) {
				idn = UUID.nameUUIDFromBytes(this.fingerprint.getBytes()).toString();
			} else {
				idn = UUID.randomUUID().toString();
			}
			this.id = httpService.getBrowserId(ArgUtil.parseAsString(idn));
		}
		this.browser = userAgent.getBrowser();
		this.browserVersion = userAgent.getBrowserVersion();
		this.operatingSystem = userAgent.getOperatingSystem();

		/**
		 * "browserVersion": null, "platform": "UNKNOWN", "id":
		 * "2673a5c9-f334-4be3-b810-418e15f9c1ae", "browser": "UNKNOWN", "fingerprint":
		 * null, "appVersion": null, "ip": "141.101.107.253", "type": "NORMAL", "os":
		 * "UNKNOWN"
		 */

		/**
		 * 
		 * "browserVersion": null, "platform": "ANDROID", "id":
		 * "ac4a66c5-af27-49f2-9388-7ae1b2f5c0c6", "browser": "UNKNOWN", "fingerprint":
		 * null, "appVersion": null, "ip": "49.32.170.141", "type": "TABLET", "os":
		 * "ANDROID7_TABLET"
		 * 
		 */
		if (this.appType == null) {

			if (this.platform == DevicePlatform.ANDROID && this.browser == Browser.UNKNOWN) {
				this.appType = AppType.ANDROID;
			} else if (this.platform == DevicePlatform.IOS && this.browser == Browser.UNKNOWN) {
				this.appType = AppType.IOS;
			} else if (this.platform == DevicePlatform.UNKNOWN && this.browser == Browser.UNKNOWN
					&& this.operatingSystem == OperatingSystem.UNKNOWN) {
				this.appType = AppType.IOS;
			} else if (this.fingerprint != null && !UIConstants.EMPTY.equalsIgnoreCase(this.fingerprint)) {
				if (this.fingerprint.length() == 16) {
					this.appType = AppType.ANDROID;
				} else if (this.fingerprint.length() == 40) {
					this.appType = AppType.IOS;
				} else if (this.fingerprint.length() == 32) {
					this.appType = AppType.WEB;
				}
			} else if (this.type == DeviceType.NORMAL) {
				this.appType = AppType.WEB;
			}
		}

		return this;
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
		map.put("appType", appType);
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
		device.setAppType(getAppType());
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

	public AppType getAppType() {
		return appType;
	}

	public void setAppType(AppType appType) {
		this.appType = appType;
	}
}
