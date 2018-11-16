package com.amx.jax.http;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.amx.jax.AppConstants;
import com.amx.jax.filter.AppParamController;
import com.amx.utils.ArgUtil;

public enum RequestType {
	DEFAULT(true, true), POLL(false, false), PING(true, false), PUBG(true, false);
	boolean track = false;
	boolean auth = true;

	RequestType(boolean track, boolean auth) {
		this.track = track;
		this.auth = auth;
	}

	public boolean isTrack() {
		return track;
	}

	public boolean isAuth() {
		return auth;
	}

	public static RequestType from(HttpServletRequest req) {
		if (req.getRequestURI().contains(AppParamController.PUB_AMX_PREFIX)) {
			return PING;
		}
		if (req.getRequestURI().contains(AppParamController.PUBG_AMX_PREFIX)) {
			return PUBG;
		}

		RequestType reqType = RequestType.DEFAULT;
		String reqTypeStr = req.getHeader(AppConstants.REQUEST_TYPE_XKEY);
		if (!StringUtils.isEmpty(reqTypeStr)) {
			reqType = (RequestType) ArgUtil.parseAsEnum(reqTypeStr, reqType);
		}
		return reqType;
	}

}