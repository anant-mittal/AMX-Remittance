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
import com.amx.jax.ui.service.HttpService.UserAgent;

@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserDevice implements Serializable {

	private static final long serialVersionUID = -6869375666742059912L;
	private String deviceId = null;
	private String deviceIp = null;
	private DeviceType deviceType = null;
	private DevicePlatform devicePlatform = null;
	private String deviceOs = null;
	private String deviceBrowser = null;

	@Autowired
	private HttpService httpService;

	private void resolve() {
		Device currentDevice = httpService.getCurrentDevice();
		this.deviceIp = httpService.getIPAddress();
		this.deviceType = (currentDevice.isMobile() ? DeviceType.MOBILE
				: (currentDevice.isTablet() ? DeviceType.TABLET : DeviceType.NORMAL));
		this.devicePlatform = currentDevice.getDevicePlatform();
		this.deviceId = httpService.getDeviceId();
		UserAgent userAgent = httpService.getUserAgent();
		this.deviceBrowser = userAgent.getBrowser();
		this.deviceOs = userAgent.getOs();
	}

	public String getDeviceIp() {
		if (deviceIp == null) {
			this.resolve();
		}
		return deviceIp;
	}

	public DeviceType getDeviceType() {
		if (deviceType == null) {
			this.resolve();
		}
		return deviceType;
	}

	public DevicePlatform getDevicePlatform() {
		if (devicePlatform == null) {
			this.resolve();
		}
		return devicePlatform;
	}

	public String getDeviceId() {
		if (deviceId == null) {
			this.resolve();
		}
		return deviceId;
	}

	public String getDeviceOs() {
		if (deviceOs == null) {
			this.resolve();
		}
		return deviceOs;
	}

	public String getDeviceBrowser() {
		if (deviceBrowser == null) {
			this.resolve();
		}
		return deviceBrowser;
	}

	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceId", deviceId);
		map.put("devicePlatform", devicePlatform);
		map.put("deviceType", deviceType);
		map.put("deviceIp", deviceIp);
		map.put("deviceBrowser", deviceBrowser);
		map.put("deviceOs", deviceOs);
		return map;
	}

}
