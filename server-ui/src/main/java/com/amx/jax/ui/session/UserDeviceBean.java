package com.amx.jax.ui.session;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DevicePlatform;
import org.springframework.mobile.device.DeviceType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.logger.LoggerService;
import com.amx.jax.service.HttpService;
import com.amx.jax.ui.UIConstants;
import com.amx.jax.user.UserDevice;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;

/**
 * The Class UserDeviceBean.
 */
@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDeviceBean extends UserDevice {

	private static final long serialVersionUID = -6869375666742059912L;

	private transient Logger logger = LoggerService.getLogger(getClass());

	@Autowired
	private transient HttpService httpService;

	/**
	 * Resolve.
	 *
	 * @return the user device
	 */
	public UserDevice resolve() {
		Device currentDevice = httpService.getCurrentDevice();
		this.ip = httpService.getIPAddress();
		if (currentDevice != null) {
			this.type = (currentDevice.isMobile() ? DeviceType.MOBILE
					: (currentDevice.isTablet() ? DeviceType.TABLET : DeviceType.NORMAL));
			this.platform = currentDevice.getDevicePlatform();
		} else {
			logger.warn("DeviceUtils by Springframework is not able to determin UserDevice");
		}

		this.fingerprint = httpService.getDeviceId();
		UserAgent userAgent = httpService.getUserAgent();

		if (this.id == null) {
			String idn = ArgUtil.parseAsString(userAgent.getId());
			// if (this.fingerprint != null) {
			// idn = UUID.nameUUIDFromBytes(this.fingerprint.getBytes()).toString();
			// } else {
			// idn = UUID.randomUUID().toString();
			// }
			this.id = httpService.setBrowserId(ArgUtil.parseAsString(idn));
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

	public boolean isDifferent() {
		if (this.id == null || this.fingerprint == null) {
			return true;
		}
		String ip = ArgUtil.parseAsString(httpService.getIPAddress(), Constants.BLANK);
		String fingerprint = ArgUtil.parseAsString(httpService.getDeviceId(), Constants.BLANK);
		UserAgent userAgent = httpService.getUserAgent();
		String id = ArgUtil.parseAsString(userAgent.getId(), Constants.BLANK);
		if (!id.equals(this.id)
				// || !fingerprint.equals(this.fingerprint)
				|| !ip.equals(this.ip) || !(this.browser == null || this.browser.equals(userAgent.getBrowser()))) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getFingerprint()
	 */
	@Override
	public String getFingerprint() {
		if (type == null) {
			this.resolve();
		}
		return fingerprint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getId()
	 */
	@Override
	public String getId() {
		if (type == null) {
			this.resolve();
		}
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getAppVersion()
	 */
	@Override
	public String getAppVersion() {
		if (type == null) {
			this.resolve();
		}
		return appVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getIp()
	 */
	@Override
	public String getIp() {
		if (type == null) {
			this.resolve();
		}
		return ip;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getType()
	 */
	@Override
	public DeviceType getType() {
		if (type == null) {
			this.resolve();
		}
		return type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getPlatform()
	 */
	@Override
	public DevicePlatform getPlatform() {
		if (type == null) {
			this.resolve();
		}
		return platform;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getOperatingSystem()
	 */
	@Override
	public OperatingSystem getOperatingSystem() {
		if (type == null) {
			this.resolve();
		}
		return operatingSystem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getBrowser()
	 */
	@Override
	public Browser getBrowser() {
		if (type == null) {
			this.resolve();
		}
		return browser;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.amx.jax.user.UserDevice#getBrowserVersion()
	 */
	@Override
	public Version getBrowserVersion() {
		if (type == null) {
			this.resolve();
		}
		return browserVersion;
	}

	/**
	 * To map.
	 *
	 * @return the map
	 */
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
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

	/**
	 * To user device.
	 *
	 * @return the user device
	 */
	public UserDevice toUserDevice() {
		UserDeviceBean device = new UserDeviceBean();
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

}
