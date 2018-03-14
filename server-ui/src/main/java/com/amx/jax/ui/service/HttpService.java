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

@Component
public class HttpService {

	private Logger LOGGER = LoggerFactory.getLogger(getClass());

	public class UserAgent {
		String os = null;
		String browser = null;

		public String getOs() {
			return os;
		}

		public void setOs(String os) {
			this.os = os;
		}

		public String getBrowser() {
			return browser;
		}

		public void setBrowser(String browser) {
			this.browser = browser;
		}
	}

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
		UserAgent agent = new UserAgent();
		if (request != null) {
			String browserDetails = request.getHeader("User-Agent");
			String userAgent = browserDetails;
			String user = userAgent.toLowerCase();

			String os = "";
			String browser = "";

			LOGGER.debug("User Agent for the request is===>{}", browserDetails);
			// =================OS=======================
			if (userAgent.toLowerCase().indexOf("windows") >= 0) {
				os = "Windows";
			} else if (userAgent.toLowerCase().indexOf("mac") >= 0) {
				os = "Mac";
			} else if (userAgent.toLowerCase().indexOf("x11") >= 0) {
				os = "Unix";
			} else if (userAgent.toLowerCase().indexOf("android") >= 0) {
				os = "Android";
			} else if (userAgent.toLowerCase().indexOf("iphone") >= 0) {
				os = "IPhone";
			} else {
				os = "UnKnown, More-Info: " + userAgent;
			}
			// ===============Browser===========================
			if (user.contains("msie")) {
				String substring = userAgent.substring(userAgent.indexOf("MSIE")).split(";")[0];
				browser = substring.split(" ")[0].replace("MSIE", "IE") + "-" + substring.split(" ")[1];
			} else if (user.contains("safari") && user.contains("version")) {
				browser = (userAgent.substring(userAgent.indexOf("Safari")).split(" ")[0]).split("/")[0] + "-"
						+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
			} else if (user.contains("opr") || user.contains("opera")) {
				if (user.contains("opera"))
					browser = (userAgent.substring(userAgent.indexOf("Opera")).split(" ")[0]).split("/")[0] + "-"
							+ (userAgent.substring(userAgent.indexOf("Version")).split(" ")[0]).split("/")[1];
				else if (user.contains("opr"))
					browser = ((userAgent.substring(userAgent.indexOf("OPR")).split(" ")[0]).replace("/", "-"))
							.replace("OPR", "Opera");
			} else if (user.contains("chrome")) {
				browser = (userAgent.substring(userAgent.indexOf("Chrome")).split(" ")[0]).replace("/", "-");
			} else if ((user.indexOf("mozilla/7.0") > -1) || (user.indexOf("netscape6") != -1)
					|| (user.indexOf("mozilla/4.7") != -1) || (user.indexOf("mozilla/4.78") != -1)
					|| (user.indexOf("mozilla/4.08") != -1) || (user.indexOf("mozilla/3") != -1)) {
				// browser=(userAgent.substring(userAgent.indexOf("MSIE")).split("
				// ")[0]).replace("/", "-");
				browser = "Netscape-?";

			} else if (user.contains("firefox")) {
				browser = (userAgent.substring(userAgent.indexOf("Firefox")).split(" ")[0]).replace("/", "-");
			} else if (user.contains("rv")) {
				browser = "IE-" + user.substring(user.indexOf("rv") + 3, user.indexOf(")"));
			} else {
				browser = "UnKnown, More-Info: " + userAgent;
			}
			LOGGER.debug("Operating System======> {}", os);
			LOGGER.debug("Browser Name==========> {} ", browser);

			agent.setBrowser(browser);
			agent.setOs(os);
		}
		return agent;

	}

}
