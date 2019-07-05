package com.amx.jax;

import com.amx.jax.dict.ContactType;
import com.amx.utils.ArgUtil;

public class JaxAuthContext {

	public static String getOtp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("otp"));
	}

	public static String getMotp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("mOtp"));
	}

	public static String getEotp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("eOtp"));
	}

	public static String getWotp() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("wOtp"));
	}
	
	public static ContactType getContactType() {
		return ArgUtil.parseAsEnumIgnoreCase(AppContextUtil.getParams().get("contactType"), ContactType.class);
	}

	public static String getAnyOtp() {
		String otp = getOtp();
		if (ArgUtil.isEmpty(otp)) {
			otp = getMotp();
		}
		if (ArgUtil.isEmpty(otp)) {
			otp = getEotp();
		}
		if (ArgUtil.isEmpty(otp)) {
			otp = getWotp();
		}
		return otp;
	}

	public static String getSecAns() {
		return ArgUtil.parseAsString(AppContextUtil.getParams().get("secAns"));
	}

	public static String otp(String otp) {
		if (ArgUtil.isEmpty(otp)) {
			return getOtp();
		}
		AppContextUtil.getParams().put("otp", otp);
		return otp;
	}

	public static String wOtp(String wOtp) {
		if (ArgUtil.isEmpty(wOtp)) {
			return getWotp();
		}
		AppContextUtil.getParams().put("wOtp", wOtp);
		return wOtp;
	}

	public static String mOtp(String mOtp) {
		if (ArgUtil.isEmpty(mOtp)) {
			return getMotp();
		}
		AppContextUtil.getParams().put("mOtp", mOtp);
		return mOtp;
	}

	public static String eOtp(String eOtp) {
		if (ArgUtil.isEmpty(eOtp)) {
			return getEotp();
		}
		AppContextUtil.getParams().put("eOtp", eOtp);
		return eOtp;
	}

	public static ContactType contactType(ContactType contactType) {
		if (ArgUtil.isEmpty(contactType)) {
			return getContactType();
		}
		AppContextUtil.getParams().put("contactType", contactType);
		return contactType;
	}

	public static String secAns(String secAns) {
		if (ArgUtil.isEmpty(secAns)) {
			return getSecAns();
		}
		AppContextUtil.getParams().put("secAns", secAns);
		return secAns;
	}

}