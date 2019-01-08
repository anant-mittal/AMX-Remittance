package com.amx.jax;

import com.amx.utils.ArgUtil;

public class JaxAuthContext {

	public static String getMotp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("mOtp"));
	}

	public static String getEotp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("eOtp"));
	}

	public static String getSecAns() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("secAns"));
	}

}