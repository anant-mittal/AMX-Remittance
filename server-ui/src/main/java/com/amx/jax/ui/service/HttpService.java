package com.amx.jax.ui.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amx.jax.ui.UIConstants;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class HttpService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	// public class UserAgent {
	// String os = null;
	// String browser = null;
	//
	// public String getOs() {
	// return os;
	// }
	//
	// public void setOs(String os) {
	// this.os = os;
	// }
	//
	// public String getBrowser() {
	// return browser;
	// }
	//
	// public void setBrowser(String browser) {
	// this.browser = browser;
	// }
	// }

	@Autowired(required = false)
	private HttpServletRequest request;

	public String getIPAddress() {
		String remoteAddr = null;
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

	public Device getCurrentDevice() {
		return DeviceUtils.getCurrentDevice(request);
	}

	public String getDeviceId() {
		String deviceId = null;
		Cookie cookie = WebUtils.getCookie(request, UIConstants.DEVICE_ID_KEY);
		if (cookie != null) {
			deviceId = cookie.getValue();
		}
		return deviceId;
	}

	public UserAgent getUserAgent() {
		UserAgent agent = new UserAgent() ;
		if (request != null) {
			String browserDetails = request.getHeader("User-Agent");
			return UserAgent.parseUserAgentString(browserDetails);
		}
		return agent;

	}

}
