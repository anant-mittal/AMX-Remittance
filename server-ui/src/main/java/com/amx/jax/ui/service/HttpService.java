package com.amx.jax.ui.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amx.jax.AppConfig;
import com.amx.jax.dict.Language;
import com.amx.jax.ui.UIConstants;
import com.amx.utils.ArgUtil;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class HttpService {

	private static final PolicyFactory policy = new HtmlPolicyBuilder().allowStandardUrlProtocols().toFactory();

	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;

	@Autowired
	private AppConfig appConfig;

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

	public Language getLanguage() {
		return (Language) ArgUtil.parseAsEnum(request.getLocale().getLanguage(), Language.DEFAULT);
	}

	public Device getCurrentDevice() {
		return DeviceUtils.getCurrentDevice(request);
	}

	public String getDeviceId() {
		String deviceId = null;
		if (request != null) {
			Cookie cookie = WebUtils.getCookie(request, UIConstants.DEVICE_ID_KEY);
			if (cookie != null) {
				deviceId = cookie.getValue();
			} else {
				deviceId = request.getHeader(UIConstants.DEVICE_ID_XKEY);
			}
		}
		return deviceId;
	}

	public void clearSessionCookie() {
		Cookie cookie = WebUtils.getCookie(request, UIConstants.SESSIONID);
		if (cookie != null) {
			cookie.setMaxAge(0);
		}
	}

	public String getBrowserId(String browserIdNew) {
		String browserId = null;
		if (request != null) {
			Cookie cookie = WebUtils.getCookie(request, UIConstants.BROWSER_ID_KEY);
			if (cookie != null) {
				browserId = cookie.getValue();
			} else if (response != null) {
				browserId = browserIdNew;
				Cookie kooky = new Cookie(UIConstants.BROWSER_ID_KEY, browserIdNew);
				kooky.setMaxAge(31622400);
				kooky.setHttpOnly(appConfig.isCookieHttpOnly());
				kooky.setSecure(appConfig.isCookieSecure());
				kooky.setPath("/");
				response.addCookie(kooky);
			}
		}
		return browserId;
	}

	public UserAgent getUserAgent() {
		UserAgent agent = new UserAgent();
		if (request != null) {
			String browserDetails = request.getHeader("User-Agent");
			return UserAgent.parseUserAgentString(browserDetails);
		}
		return agent;
	}

	public static String sanitze(String str) {
		return policy.sanitize(str);
	}

}
