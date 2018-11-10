package com.amx.utils;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {

	public static String getIPAddress(HttpServletRequest request) {
		String remoteAddr = null;
		if (request != null) {
			remoteAddr = request.getHeader("X-FORWARDED-FOR");
			if (remoteAddr == null || "".equals(remoteAddr)) {
				remoteAddr = request.getRemoteAddr();
			}
		}
		return remoteAddr;
	}

	public static String getServerName(HttpServletRequest request) {
		String scheme = request.getScheme();
		String serverName = request.getServerName();
		int portNumber = request.getServerPort();
		String portStr = "";
		if (portNumber != 80 && portNumber != 443) {
			portStr = ":" + portNumber;
		}
		return scheme + "://" + serverName + portStr;
	}

}
