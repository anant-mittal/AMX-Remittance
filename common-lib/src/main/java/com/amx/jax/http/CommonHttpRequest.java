package com.amx.jax.http;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.dict.Language;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;

@Component
public class CommonHttpRequest {

	public static class CommonMediaType extends MediaType {

		private static final long serialVersionUID = -540693758487754320L;

		/**
		 * Public constant media type for {@code application/v0+json}.
		 * 
		 * @see #APPLICATION_JSON_UTF8
		 */
		public final static MediaType APPLICATION_V0_JSON;

		/**
		 * A String equivalent of {@link MediaType#APPLICATION_V0_JSON}.
		 * 
		 * @see #APPLICATION_JSON_UTF8_VALUE
		 */
		public final static String APPLICATION_V0_JSON_VALUE = "application/v0+json";

		public CommonMediaType(String type, String subtype) {
			super(type, subtype);
		}

		public final static String[] APPLICATION_JSON_VALUES = { MediaType.APPLICATION_JSON_VALUE,
				APPLICATION_V0_JSON_VALUE };

		static {
			APPLICATION_V0_JSON = valueOf(APPLICATION_JSON_VALUE);
		}

	}

	private static final PolicyFactory policy = new HtmlPolicyBuilder().allowStandardUrlProtocols().toFactory();

	@Autowired(required = false)
	private HttpServletRequest request;

	@Autowired(required = false)
	private HttpServletResponse response;

	@Autowired
	private AppConfig appConfig;

	public String getIPAddress() {
		return HttpUtils.getIPAddress(request);
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
			Cookie cookie = WebUtils.getCookie(request, AppConstants.DEVICE_ID_KEY);
			if (cookie != null) {
				deviceId = cookie.getValue();
			} else {
				deviceId = request.getHeader(AppConstants.DEVICE_ID_XKEY);
			}
		}
		return deviceId;
	}

	public void clearSessionCookie() {
		Cookie cookie = WebUtils.getCookie(request, AppConstants.SESSIONID);
		if (cookie != null) {
			cookie.setMaxAge(0);
		}
	}

	public String setBrowserId(String browserIdNew) {
		String browserId = null;
		if (request != null) {
			// Cookie cookie = WebUtils.getCookie(request, AppConstants.BROWSER_ID_KEY);
			// if (cookie != null) {
			// browserId = cookie.getValue();
			// } else
			if (response != null && browserIdNew != null) {
				browserId = browserIdNew;
				Cookie kooky = new Cookie(AppConstants.BROWSER_ID_KEY, browserIdNew);
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
		UserAgent agent = new UserAgent(OperatingSystem.UNKNOWN, Browser.UNKNOWN);
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
